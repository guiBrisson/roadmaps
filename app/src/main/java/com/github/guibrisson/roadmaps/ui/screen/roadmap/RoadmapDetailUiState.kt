package com.github.guibrisson.roadmaps.ui.screen.roadmap

import com.github.guibrisson.model.RoadmapDetail

sealed interface RoadmapDetailUiState {
    data object Loading : RoadmapDetailUiState
    data class Success(val detail: RoadmapDetail) : RoadmapDetailUiState
    data class Failure(val message: String) : RoadmapDetailUiState
}
