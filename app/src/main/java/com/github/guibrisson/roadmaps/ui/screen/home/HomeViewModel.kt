package com.github.guibrisson.roadmaps.ui.screen.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.guibrisson.data.repository.RoadmapRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val roadmapRepository: RoadmapRepository,
) : ViewModel() {
    private val _roadmapsUiState = MutableStateFlow<RoadmapsUiState>(RoadmapsUiState.Loading)
    val roadmapUiState: StateFlow<RoadmapsUiState> = _roadmapsUiState.asStateFlow()

    fun fetchAllRoadmaps() {
        viewModelScope.launch {
            try {
                val roadmaps = roadmapRepository.listAllRoadmaps()
                _roadmapsUiState.update { RoadmapsUiState.Success(roadmaps) }
            } catch (e: Exception) {
                e.printStackTrace()
                _roadmapsUiState.update { RoadmapsUiState.Failure("An unexpected error occurred") }
            }
        }
    }
}
