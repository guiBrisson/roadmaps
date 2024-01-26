package com.github.guibrisson.roadmaps.ui.screen.roadmap_item

import com.github.guibrisson.model.TopicItem

sealed interface ItemUiState {
    data object Loading : ItemUiState
    data class Success(val item: TopicItem, val roadmapId: String,) : ItemUiState
    data class Failure(val errorMessage: String) : ItemUiState
}

fun ItemUiState.isSuccessful() : Boolean {
    return when(this) {
        is ItemUiState.Success -> true
        else -> false
    }
}
