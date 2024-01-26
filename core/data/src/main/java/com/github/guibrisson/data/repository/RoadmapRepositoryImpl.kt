package com.github.guibrisson.data.repository

import android.content.Context
import com.github.guibrisson.data.service.RoadmapService
import com.github.guibrisson.model.Roadmap
import com.github.guibrisson.model.RoadmapDetail
import com.github.guibrisson.model.TopicFolder
import com.github.guibrisson.model.TopicSystem
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RoadmapRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
) : RoadmapRepository {
    private val service = RoadmapService(context)

    override suspend fun listAllRoadmaps(): List<Roadmap> {
        return service.getAllRoadmaps()
    }

    override suspend fun getRoadmapDetails(roadmapId: String): RoadmapDetail? {
        return service.getRoadmapDetails(roadmapId)
    }

    override suspend fun getRoadmapFolder(roadmapId: String, folderId: String): TopicFolder? {
        val detail = service.getRoadmapDetails(roadmapId) ?: return null
        val folders = getAllFolders(detail.content.topics)
        return folders.firstOrNull { it.id == folderId }
    }

    private fun getAllFolders(items: List<TopicSystem>): List<TopicFolder> {
        val folders = items.filterIsInstance<TopicFolder>()
        val folderMutableList: MutableList<TopicFolder> = folders.toMutableList()

        if (folders.isNotEmpty()) {
            folders.forEach { folder ->
                folderMutableList.addAll(getAllFolders(folder.topics))
            }
        }

        return folderMutableList
    }
}
