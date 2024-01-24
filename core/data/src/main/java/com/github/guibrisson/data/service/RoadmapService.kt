package com.github.guibrisson.data.service

import android.content.Context
import android.content.res.AssetManager
import android.util.Log
import com.github.guibrisson.model.Roadmap
import com.github.guibrisson.model.RoadmapDetail
import com.github.guibrisson.model.TopicFolder
import com.github.guibrisson.model.TopicItem
import kotlin.streams.toList

class RoadmapService(private val context: Context) {
    private val roadmaps = context.assets.list("roadmaps")

    fun getAllRoadmaps(): List<Roadmap> {
        val roadmaps = roadmaps?.map { parseRoadmap(it) }.orEmpty()
        Log.d(TAG, "getAllRoadmaps: size[${roadmaps.size}]")
        return roadmaps
    }

    fun getRoadmapDetails(roadmapId: String): RoadmapDetail? {
        if (roadmaps?.firstOrNull { it ==  roadmapId } == null) {
            return null
        }

        val roadmap = parseRoadmap(roadmapId)
        val topics = buildRoadmapAssetTree(context.assets, "roadmaps/$roadmapId/content")

        return RoadmapDetail(
            id = roadmap.id,
            name = roadmap.name,
            description = roadmap.description,
            topics = topics,
        ).also { Log.d(TAG, "getRoadmapDetails: ${it.id}") }
    }

    private fun buildRoadmapAssetTree(
        assetManager: AssetManager,
        roadmapPath: String,
    ): TopicFolder {
        val rootId = roadmapPath.substringAfterLast("/").substringAfter("-")
        val rootItems = assetManager.list(roadmapPath)?.map { asset ->
            val id = asset.substringBefore(".").substringAfter("-")
            val assetPath = "$roadmapPath/$asset"
            if (assetManager.list(assetPath)?.isNotEmpty() == true) {
                buildRoadmapAssetTree(assetManager, assetPath)
            } else {
                val br = assetManager.open(assetPath).bufferedReader()
                var name = ""
                var content = ""

                br.lines().toList().forEachIndexed { index, s ->
                    when (index) {
                        0 -> name = s
                        else -> content += s
                    }
                }

                TopicItem(
                    id = id,
                    name = name.substringAfter("#").trim(),
                    content = content.trim()
                )
            }
        }?.toMutableList() ?: mutableListOf()

        val indexItem = rootItems.last()
        rootItems.removeLast()

        return TopicFolder(
            id = rootId,
            name = indexItem.name,
            content = indexItem.content,
            topics = rootItems,
        )
    }

    private fun parseRoadmap(roadmapId: String): Roadmap {
        var name = ""
        var description = ""

        val mdAsset = context.assets.open("roadmaps/$roadmapId/$roadmapId.md")
        val bf = mdAsset.bufferedReader()

        for (line in bf.lines()) {
            when {
                line.contains("title") && name.isEmpty() -> {
                    name = parseMdLine(line).orEmpty()
                }

                line.contains("description") && description.isEmpty() -> {
                    description = parseMdLine(line).orEmpty()
                }

                name.isNotEmpty() && description.isNotEmpty() -> break
            }
        }

        return Roadmap(roadmapId, name, description)
    }

    private fun parseMdLine(line: String): String? {
        val strings = line.split(":")
        return strings.lastOrNull()?.trim()?.replace("'", "")
    }

    companion object {
        private val TAG = RoadmapService::class.simpleName
    }
}