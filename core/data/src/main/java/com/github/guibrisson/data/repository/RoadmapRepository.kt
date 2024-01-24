package com.github.guibrisson.data.repository

import com.github.guibrisson.model.Roadmap
import com.github.guibrisson.model.RoadmapDetail

interface RoadmapRepository {
    suspend fun listAllRoadmaps(): List<Roadmap>
    suspend fun getRoadmapDetails(roadmapId: String): RoadmapDetail?
}
