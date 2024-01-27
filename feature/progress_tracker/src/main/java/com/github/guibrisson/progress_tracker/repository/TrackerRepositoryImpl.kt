package com.github.guibrisson.progress_tracker.repository

import android.content.Context
import com.github.guibrisson.progress_tracker.ProgressTrackerManager
import com.github.guibrisson.progress_tracker.model.RoadmapTracker
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class TrackerRepositoryImpl @Inject constructor(
    @ApplicationContext context: Context,
) : TrackerRepository {
    private val tracker = ProgressTrackerManager(context)
    private val _roadmapsFlow = MutableStateFlow(roadmaps())

    override fun roadmapsTracker(): StateFlow<List<RoadmapTracker>> = _roadmapsFlow.asStateFlow()

    override suspend fun getRoadmapTracker(roadmapId: String): RoadmapTracker? {
        val roadmaps = tracker.readFile()
        _roadmapsFlow.update { roadmaps() }
        return roadmaps.firstOrNull { it.roadmapId == roadmapId }
    }

    override suspend fun clearTracker() {
        tracker.emptyFileList()
        _roadmapsFlow.update { roadmaps() }
    }

    override suspend fun toggleFavorite(roadmapId: String) {
        val roadmaps = tracker.readFile()
        roadmaps.firstOrNull { it.roadmapId == roadmapId }?.let { roadmapTracker ->
            val isFavorite = roadmapTracker.isFavorite
            val updatedRoadmap = roadmapTracker.copy(isFavorite = !isFavorite)
            tracker.writeOnFile(updatedRoadmap)
            _roadmapsFlow.update { roadmaps() }
        }
    }

    private fun roadmaps() = tracker.readFile()
}
