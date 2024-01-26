package com.github.guibrisson.data.repository

import com.github.guibrisson.model.Roadmap
import com.github.guibrisson.model.RoadmapDetail
import com.github.guibrisson.model.TopicFolder
import com.github.guibrisson.model.TopicItem

interface RoadmapRepository {
    suspend fun listAllRoadmaps(): List<Roadmap>
    suspend fun getRoadmapDetails(roadmapId: String): RoadmapDetail?
    suspend fun getRoadmapFolder(roadmapId: String, folderId: String): TopicFolder?
    suspend fun getRoadmapItem(roadmapId: String, itemId: String): TopicItem?
}
