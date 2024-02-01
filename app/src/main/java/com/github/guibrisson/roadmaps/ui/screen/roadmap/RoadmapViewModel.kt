package com.github.guibrisson.roadmaps.ui.screen.roadmap

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.guibrisson.data.repository.RoadmapRepository
import com.github.guibrisson.progress_tracker.repository.TrackerRepository
import com.github.guibrisson.roadmaps.navigation.NavigationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RoadmapViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val roadmapRepository: RoadmapRepository,
    private val trackerRepository: TrackerRepository,
) : ViewModel() {
    private val roadmapId: String = checkNotNull(savedStateHandle[NavigationUtils.ROADMAP_ID_ARG])
    private val _uiState = MutableStateFlow<RoadmapDetailUiState>(RoadmapDetailUiState.Loading)
    val uiState: StateFlow<RoadmapDetailUiState> = _uiState.asStateFlow()

    fun fetchRoadmapDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                roadmapRepository.getRoadmapDetails(roadmapId)?.let { details ->
                    val tracker = trackerRepository.getRoadmapTracker(roadmapId)
                    val isFavorite = tracker?.isFavorite ?: false
                    val progress = tracker?.progress ?: emptyList()

                    details.isFavorite = isFavorite
                    details.progress = progress
                    _uiState.update { RoadmapDetailUiState.Success(details) }
                    return@launch
                }
                val errorMessage = "No roadmap with id '$roadmapId'"
                _uiState.update { RoadmapDetailUiState.Failure(errorMessage) }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update { RoadmapDetailUiState.Failure("An unexpected error occurred") }
            }
        }
    }

    fun favorite(roadmapId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            trackerRepository.toggleFavorite(roadmapId)
        }

        fetchRoadmapDetails()
    }
}
