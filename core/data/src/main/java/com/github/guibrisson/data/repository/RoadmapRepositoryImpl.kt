package com.github.guibrisson.data.repository

import android.content.Context
import com.github.guibrisson.data.service.RoadmapService
import com.github.guibrisson.model.Roadmap
import com.github.guibrisson.model.RoadmapDetail
import com.github.guibrisson.model.TopicFolder
import com.github.guibrisson.model.TopicItem
import com.github.guibrisson.model.TopicSystem
import com.github.guibrisson.progress_tracker.repository.TrackerRepository
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RoadmapRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
    trackerRepository: TrackerRepository,
) : RoadmapRepository {
    private val service = RoadmapService(context, trackerRepository)

    override fun listAllRoadmaps(): List<Roadmap> {
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

    override suspend fun getRoadmapItem(roadmapId: String, itemId: String): TopicItem? {
        val detail = service.getRoadmapDetails(roadmapId) ?: return null
        val items = getAllItems(detail.content.topics)
        return items.firstOrNull { it.id == itemId }
    }

    private fun getAllFolders(topics: List<TopicSystem>): List<TopicFolder> {
        val folders = topics.filterIsInstance<TopicFolder>()
        val folderMutableList: MutableList<TopicFolder> = folders.toMutableList()

        if (folders.isNotEmpty()) {
            folders.forEach { folder ->
                folderMutableList.addAll(getAllFolders(folder.topics))
            }
        }

        return folderMutableList
    }

    private fun getAllItems(topics: List<TopicSystem>): List<TopicItem> {
        val items = topics.filterIsInstance<TopicItem>()
        val folderMutableList: MutableList<TopicItem> = items.toMutableList()

        for (item in topics) {
            when(item) {
                is TopicFolder -> folderMutableList.addAll(getAllItems(item.topics))
                is TopicItem -> folderMutableList.add(item)
            }
        }

        return folderMutableList
    }
}
