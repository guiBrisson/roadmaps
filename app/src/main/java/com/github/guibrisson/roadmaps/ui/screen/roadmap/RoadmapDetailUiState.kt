package com.github.guibrisson.roadmaps.ui.screen.roadmap

import com.github.guibrisson.model.RoadmapDetail

sealed interface RoadmapDetailUiState {
    data object Loading : RoadmapDetailUiState
    data class Success(val detail: RoadmapDetail) : RoadmapDetailUiState
    data class Failure(val message: String) : RoadmapDetailUiState
}

fun RoadmapDetailUiState.isSuccessful(): Boolean {
    return when(this) {
        is RoadmapDetailUiState.Success -> true
        else -> false
    }
}

fun RoadmapDetailUiState.isLoading(): Boolean {
    return when(this) {
        is RoadmapDetailUiState.Loading -> true
        else -> false
    }
}
