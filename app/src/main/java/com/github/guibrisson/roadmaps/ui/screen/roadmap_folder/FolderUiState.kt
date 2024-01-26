package com.github.guibrisson.roadmaps.ui.screen.roadmap_folder

import com.github.guibrisson.model.TopicFolder

sealed interface FolderUiState {
    data object Loading : FolderUiState
    data class Success(val folder: TopicFolder, val roadmapId: String,) : FolderUiState
    data class Failure(val errorMessage: String) : FolderUiState
}

fun FolderUiState.isSuccessful() : Boolean {
    return when (this) {
        is FolderUiState.Success -> true
        else -> false
    }
}
