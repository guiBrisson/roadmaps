package com.github.guibrisson.roadmaps.ui.screen.roadmap_folder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.guibrisson.data.repository.RoadmapRepository
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
class RoadmapFolderViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val roadmapRepository: RoadmapRepository,
) : ViewModel() {
    private val navArgs: String =
        checkNotNull(savedStateHandle[NavigationUtils.ROADMAP_FOLDER_ARGS])
    private val navArgsList: List<String> = navArgs.split(",")
    private val roadmapId: String = navArgsList.first()
    private val folderId = navArgsList[1].trim()

    private val _uiState = MutableStateFlow<FolderUiState>(FolderUiState.Loading)
    val uiState: StateFlow<FolderUiState> = _uiState.asStateFlow()

    fun fetchFolder() {
        viewModelScope.launch(Dispatchers.IO) {
            roadmapRepository.getRoadmapFolder(roadmapId, folderId)?.let { folder ->
                _uiState.update { FolderUiState.Success(folder, roadmapId) }
                return@launch
            }

            val errorMessage = "Could not find topic '$folderId' from '$roadmapId'"
            _uiState.update { FolderUiState.Failure(errorMessage) }
        }
    }
}
