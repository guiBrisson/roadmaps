package com.github.guibrisson.roadmaps.ui.screen.roadmap

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
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
import com.github.guibrisson.roadmaps.ui.components.topics
import com.github.guibrisson.roadmaps.ui.theme.RoadmapsTheme

@Composable
fun RoadmapRoute(
    modifier: Modifier = Modifier,
    viewModel: RoadmapViewModel = hiltViewModel(),
    onBack: () -> Unit,
    onFolder: (Array<String>) -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchRoadmapDetails()
    }

    RoadmapScreen(
        modifier = modifier,
        uiState = uiState,
        onBack = onBack,
        onFolder = onFolder,
    )
}

@Composable
internal fun RoadmapScreen(
    modifier: Modifier = Modifier,
    uiState: RoadmapDetailUiState,
    onBack: () -> Unit,
    onFolder: (Array<String>) -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = alignLastItemToBottom()
    ) {
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

        when (uiState) {
            is RoadmapDetailUiState.Success -> roadmapDetailSuccess(
                uiState = uiState,
                onFolder = onFolder,
            )

            RoadmapDetailUiState.Loading -> loading(modifier = Modifier.padding(horizontal = 20.dp))
            is RoadmapDetailUiState.Failure -> failure(
                modifier = Modifier.padding(horizontal = 20.dp),
                errorMessage = uiState.message,
            )
        }

    }
}

private fun LazyListScope.roadmapDetailSuccess(
    uiState: RoadmapDetailUiState.Success,
    onFolder: (Array<String>) -> Unit,
) {
    item {
        val name = "@${uiState.detail.name.lowercase()}"
        Text(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 8.dp),
            text = name,
            textDecoration = TextDecoration.Underline,
            style = MaterialTheme.typography.titleMedium,
        )
    }

    item {
        Text(
            modifier = Modifier.padding(start = 20.dp, end = 20.dp, bottom = 36.dp),
            text = uiState.detail.description,
            textDecoration = TextDecoration.Underline,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
        )
    }

    topics(
        detailId = uiState.detail.id,
        topics = uiState.detail.content.topics,
        onFolder = onFolder,
        onItem = { /*TODO*/ },
    )

    item { Spacer(modifier = Modifier.padding(1.dp)) }
}

@Preview
@Composable
fun PreviewRoadmapScreen() {
    RoadmapsTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val uiState = RoadmapDetailUiState.Failure("An unexpected error occurred")

            RoadmapScreen(uiState = uiState, onBack = { }, onFolder = { })
        }
    }
}
