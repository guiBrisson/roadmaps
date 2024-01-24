package com.github.guibrisson.data.repository

import android.content.Context
import com.github.guibrisson.data.service.RoadmapService
import com.github.guibrisson.model.Roadmap
import com.github.guibrisson.model.RoadmapDetail
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
}
