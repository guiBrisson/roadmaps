package com.github.guibrisson.roadmaps.ui.screen.roadmap_folder

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.github.guibrisson.data.repository.RoadmapRepository
import com.github.guibrisson.model.TopicFolder
import com.github.guibrisson.roadmaps.navigation.NavigationRoutes
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
        checkNotNull(savedStateHandle[NavigationRoutes.ROADMAP_FOLDER_ARGS])
    private val navArgsList: List<String> = navArgs.split(",")
    private val roadmapId: String = navArgsList.first()

    private val _uiState = MutableStateFlow<FolderUiState>(FolderUiState.Loading)
    val uiState: StateFlow<FolderUiState> = _uiState.asStateFlow()

    fun fetchFolder() {
        viewModelScope.launch(Dispatchers.IO) {
            roadmapRepository.getRoadmapDetails(roadmapId)?.let { detail ->
                val folders = detail.content.topics.filterIsInstance<TopicFolder>()
                val topicId = navArgsList[1].trim()
                folders.firstOrNull { it.id == topicId }?.let { folder ->
                    _uiState.update {
                        FolderUiState.Success(
                            folder = folder,
                            roadmapId = roadmapId,
                        )
                    }
                    return@launch
                }
                _uiState.update { FolderUiState.Failure("Could not find topic '$topicId'") }
                return@launch
            }

            _uiState.update { FolderUiState.Failure("No roadmap with id '$roadmapId'") }
        }
    }
}
