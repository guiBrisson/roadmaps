package com.github.guibrisson.roadmaps.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.guibrisson.data.repository.RoadmapRepository
import com.github.guibrisson.progress_tracker.repository.TrackerRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    roadmapRepository: RoadmapRepository,
    private val trackerRepository: TrackerRepository,
) : ViewModel() {
    private val roadmaps = trackerRepository.roadmapsTracker().map { trackers ->
        roadmapRepository.listAllRoadmaps().map { roadmap ->
            val isFavorite = trackers.firstOrNull { it.roadmapId == roadmap.id }?.isFavorite ?: false
            roadmap.copy(isFavorite = isFavorite)
        }
    }

    private val _roadmapsUiState = MutableStateFlow<RoadmapsUiState>(RoadmapsUiState.Loading)
    val roadmapUiState: StateFlow<RoadmapsUiState> =
        combine(roadmaps, _roadmapsUiState) { tracker, state ->
            val saved = tracker.filter { it.isFavorite }
            val rest = tracker.filter { !it.isFavorite }
            _roadmapsUiState.update { RoadmapsUiState.Success(saved, rest) }
            state
        }.catch { t ->
            _roadmapsUiState.update {
                RoadmapsUiState.Failure(t.message ?: "An unexpected error occurred")
            }
        }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(), _roadmapsUiState.value)


    fun updateTrackedRoadmaps() {
        trackerRepository.updateRoadmapsTracker()
    }

    fun favorite(roadmapId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            trackerRepository.toggleFavorite(roadmapId)
        }
    }
}
