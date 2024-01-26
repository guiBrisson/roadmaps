package com.github.guibrisson.roadmaps.ui.screen.roadmap_folder

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.guibrisson.roadmaps.R
import com.github.guibrisson.roadmaps.ui.components.alignLastItemToBottom
import com.github.guibrisson.roadmaps.ui.components.failure
import com.github.guibrisson.roadmaps.ui.components.loading
import com.github.guibrisson.roadmaps.ui.components.topicContent
import com.github.guibrisson.roadmaps.ui.components.topics
import com.github.guibrisson.roadmaps.ui.theme.RoadmapsTheme

@Composable
fun RoadmapFolderRoute(
    modifier: Modifier = Modifier,
    viewModel: RoadmapFolderViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onFolder: (Array<String>) -> Unit,
    onItem: (Array<String>) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchFolder()
    }

    RoadmapFolderScreen(
        modifier = modifier,
        uiState = uiState,
        onBack = onBack,
        onFolder = onFolder,
        onItem = onItem,
    )
}

@Composable
internal fun RoadmapFolderScreen(
    modifier: Modifier = Modifier,
    uiState: FolderUiState,
    onBack: () -> Unit,
    onFolder: (Array<String>) -> Unit,
    onItem: (Array<String>) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = alignLastItemToBottom(),
    ) {
        item {
            Row(
                modifier = Modifier.padding(start = 2.dp, bottom = 10.dp, top = 28.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {

                IconButton(onClick = onBack) {
                    Icon(
                        Icons.Rounded.KeyboardArrowLeft,
                        contentDescription = stringResource(id = R.string.arrow_back_icon_content_description),
                    )
                }

                if (uiState.isSuccessful()) {
                    uiState as FolderUiState.Success

                    LazyRow(modifier = Modifier.padding(start = 2.dp)) {
                        item {
                            val name = "@${uiState.roadmapId.lowercase()}"
                            Text(
                                text = name,
                                textDecoration = TextDecoration.Underline,
                            )
                        }
                    }
                }
            }
        }

        when (uiState) {
            is FolderUiState.Success -> folderSuccess(
                uiState = uiState,
                onFolder = onFolder,
                onItem = onItem,
            )

            FolderUiState.Loading -> loading(modifier = Modifier.padding(horizontal = 20.dp))
            is FolderUiState.Failure -> failure(
                modifier = Modifier.padding(horizontal = 20.dp),
                errorMessage = uiState.errorMessage,
            )
        }
    }
}

private fun LazyListScope.folderSuccess(
    uiState: FolderUiState.Success,
    onFolder: (Array<String>) -> Unit,
    onItem: (Array<String>) -> Unit,
) {
    topicContent(uiState.folder)

    if (uiState.folder.topics.isNotEmpty()) {
        item {
            Text(
                modifier = Modifier
                    .padding(start = 20.dp, end = 20.dp, top = 40.dp, bottom = 20.dp),
                text = stringResource(id = R.string.progress),
                style = MaterialTheme.typography.bodyLarge,
                textDecoration = TextDecoration.Underline,
            )
        }

        topics(
            detailId = uiState.roadmapId,
            topics = uiState.folder.topics,
            onFolder = onFolder,
            onItem = onItem,
        )
    }

    item { Spacer(modifier = Modifier.padding(1.dp)) }
}

@Preview
@Composable
private fun PreviewRoadmapFolderScreen() {
    RoadmapsTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val uiState = FolderUiState.Loading
            RoadmapFolderScreen(uiState = uiState, onBack = { }, onFolder = { }, onItem = { })
        }
    }
}
