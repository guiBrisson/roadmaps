package com.github.guibrisson.progress_tracker.repository

import com.github.guibrisson.progress_tracker.model.RoadmapTracker
import kotlinx.coroutines.flow.StateFlow

interface TrackerRepository {
    fun roadmapsTracker(): StateFlow<List<RoadmapTracker>>
    fun updateRoadmapsTracker()
    fun getRoadmapTracker(roadmapId: String): RoadmapTracker?
    suspend fun clearTracker()
    suspend fun toggleFavorite(roadmapId: String)
    fun markItemAsDone(roadmapId: String, itemId: String)
}
