package com.github.guibrisson.roadmaps.ui.screen.roadmap_item

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
class RoadmapItemViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val roadmapRepository: RoadmapRepository,
) : ViewModel() {
    private val navArgs: String =
        checkNotNull(savedStateHandle[NavigationUtils.ROADMAP_TOPIC_ARGS])
    private val navArgsList: List<String> = navArgs.split(",")
    private val roadmapId: String = navArgsList.first()
    private val itemId = navArgsList[1].trim()

    private val _uiState = MutableStateFlow<ItemUiState>(ItemUiState.Loading)
    val uiState: StateFlow<ItemUiState> = _uiState.asStateFlow()

    fun fetchItem() {
        viewModelScope.launch(Dispatchers.IO) {
            roadmapRepository.getRoadmapItem(roadmapId, itemId)?.let { item ->
                _uiState.update { ItemUiState.Success(item, roadmapId) }
                return@launch
            }

            val errorMessage = "Could not find topic '$itemId' from '$roadmapId'"
            _uiState.update { ItemUiState.Failure(errorMessage) }
        }
    }
}
