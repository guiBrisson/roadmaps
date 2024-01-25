package com.github.guibrisson.roadmaps.ui.screen.roadmap_folder

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.guibrisson.roadmaps.R
import com.github.guibrisson.roadmaps.ui.components.alignLastItemToBottom
import com.github.guibrisson.roadmaps.ui.components.failure
import com.github.guibrisson.roadmaps.ui.components.loading
import com.github.guibrisson.roadmaps.ui.theme.RoadmapsTheme

@Composable
fun RoadmapFolderRoute(
    modifier: Modifier = Modifier,
    viewModel: RoadmapFolderViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    RoadmapFolderScreen(
        modifier = modifier,
        uiState = uiState,
        onBack = onBack,
    )
}

@Composable
internal fun RoadmapFolderScreen(
    modifier: Modifier = Modifier,
    uiState: FolderUiState,
    onBack: () -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = alignLastItemToBottom(),
    ) {
        item {
            LazyRow {
                item {
                    IconButton(
                        modifier = Modifier.padding(start = 2.dp, bottom = 2.dp, top = 28.dp),
                        onClick = onBack,
                    ) {
                        Icon(
                            Icons.Rounded.KeyboardArrowLeft,
                            contentDescription = stringResource(id = R.string.arrow_back_icon_content_description),
                        )
                    }
                }
            }
        }

        when (uiState) {
            is FolderUiState.Success -> {
                /*TODO*/

                item { Spacer(modifier = Modifier.padding(1.dp)) }
            }

            FolderUiState.Loading -> loading(modifier = Modifier.padding(horizontal = 20.dp))
            is FolderUiState.Failure -> failure(
                modifier = Modifier.padding(horizontal = 20.dp),
                errorMessage = uiState.errorMessage,
            )
        }
    }
}

@Preview
@Composable
private fun PreviewRoadmapFolderScreen() {
    RoadmapsTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val uiState = FolderUiState.Loading
            RoadmapFolderScreen(uiState = uiState, onBack = { })
        }
    }
}
