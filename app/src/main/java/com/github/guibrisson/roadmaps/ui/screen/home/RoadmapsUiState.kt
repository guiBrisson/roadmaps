package com.github.guibrisson.roadmaps.ui.screen.home

import com.github.guibrisson.model.Roadmap

sealed interface RoadmapsUiState {
    data object Loading : RoadmapsUiState

    data class Success(
        val savedRoadmaps: List<Roadmap>,
        val roadmaps: List<Roadmap>
    ) : RoadmapsUiState

    data class Failure(
        val errorMessage: String
    ) : RoadmapsUiState
}
