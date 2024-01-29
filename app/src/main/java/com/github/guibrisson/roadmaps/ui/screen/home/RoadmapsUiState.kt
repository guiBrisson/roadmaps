package com.github.guibrisson.roadmaps.ui.screen.home

import com.github.guibrisson.model.TrackedRoadmap

sealed interface RoadmapsUiState {
    data object Loading : RoadmapsUiState

    data class Success(
        val roadmaps: List<TrackedRoadmap>
    ) : RoadmapsUiState

    data class Failure(
        val errorMessage: String
    ) : RoadmapsUiState
}
