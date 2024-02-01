package com.github.guibrisson.progress_tracker.repository

import android.content.Context
import com.github.guibrisson.progress_tracker.ProgressTrackerManager
import com.github.guibrisson.progress_tracker.model.RoadmapTracker
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import javax.inject.Inject

class TrackerRepositoryImpl @Inject constructor(context: Context) : TrackerRepository {
    private val tracker = ProgressTrackerManager(context)
    private val _trackersFlow: MutableStateFlow<List<RoadmapTracker>> = MutableStateFlow(trackers())

    override fun roadmapsTracker(): StateFlow<List<RoadmapTracker>> = _trackersFlow.asStateFlow()

    override fun updateRoadmapsTracker() = transaction { }

    override suspend fun getRoadmapTracker(roadmapId: String): RoadmapTracker? {
        return transaction {
            val roadmaps = tracker.readFile()
            return@transaction roadmaps.firstOrNull { it.roadmapId == roadmapId }
        }
    }

    override suspend fun clearTracker() {
        return transaction { tracker.emptyFileList() }
    }

    override suspend fun toggleFavorite(roadmapId: String) {
        return transaction {
            val trackers = tracker.readFile()
            trackers.firstOrNull { it.roadmapId == roadmapId }?.let { roadmapTracker ->
                val isFavorite = roadmapTracker.isFavorite
                val updatedRoadmap = roadmapTracker.copy(isFavorite = !isFavorite)
                tracker.writeOnFile(updatedRoadmap)
                return@transaction
            }

            val roadmapTracker = RoadmapTracker(
                roadmapId = roadmapId,
                progress = emptyList(),
                isFavorite = true,
                topicsAmount =  0,
            )
            tracker.writeOnFile(roadmapTracker)
        }
    }

    private fun trackers(): List<RoadmapTracker> = tracker.readFile()

    private fun <T> transaction(function: () -> T): T {
        return function().also { _trackersFlow.update { trackers() } }
    }
}
