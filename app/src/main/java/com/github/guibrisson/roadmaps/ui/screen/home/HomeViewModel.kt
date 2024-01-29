package com.github.guibrisson.roadmaps.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.guibrisson.data.repository.RoadmapRepository
import com.github.guibrisson.model.TrackedRoadmap
import com.github.guibrisson.progress_tracker.repository.TrackerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    roadmapRepository: RoadmapRepository,
    private val trackerRepository: TrackerRepository,
) : ViewModel() {
    private val roadmaps = roadmapRepository.listAllRoadmaps()
    private val trackedRoadmaps = trackerRepository.roadmapsTracker()
        .map { trackers ->
            roadmaps.map { roadmap ->
                val isFavorite =
                    trackers.firstOrNull { it.roadmapId == roadmap.id }?.isFavorite ?: false
                TrackedRoadmap(roadmap.id, roadmap.name, roadmap.description, isFavorite)
            }
        }
        .onEach { trackedRoadmaps ->
            _roadmapsUiState.update { RoadmapsUiState.Success(trackedRoadmaps) }
        }
        .catch {  t ->
            _roadmapsUiState.update {
                val errorMessage = t.message ?: "An unexpected error occurred"
                RoadmapsUiState.Failure(errorMessage)
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), emptyList())

    private val _roadmapsUiState = MutableStateFlow<RoadmapsUiState>(RoadmapsUiState.Loading)
    val roadmapUiState: StateFlow<RoadmapsUiState> =
        combine(trackedRoadmaps, _roadmapsUiState) { tracker, _ ->
            RoadmapsUiState.Success(tracker)
        }.catch {
            RoadmapsUiState.Failure(it.message ?: "An unexpected error occurred")
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), _roadmapsUiState.value)


    fun favorite(roadmapId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            trackerRepository.toggleFavorite(roadmapId)
        }
    }
}
