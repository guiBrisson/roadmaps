package com.github.guibrisson.roadmaps.ui.screen.roadmap

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.guibrisson.roadmaps.R
import com.github.guibrisson.roadmaps.ui.theme.RoadmapsTheme

@Composable
fun RoadmapRoute(
    modifier: Modifier = Modifier,
    viewModel: RoadmapViewModel = hiltViewModel(),
    onBack: () -> Unit,
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchRoadmapDetails()
    }

    RoadmapScreen(
        modifier = modifier,
        uiState = uiState,
        onBack = onBack,
    )
}

@Composable
internal fun RoadmapScreen(
    modifier: Modifier = Modifier,
    uiState: RoadmapDetailUiState,
    onBack: () -> Unit,
) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
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

        if (uiState.isSuccessful()) {
            uiState as RoadmapDetailUiState.Success

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

            itemsIndexed(uiState.detail.topics.topics) { index, topic ->
                val paddingValues = if (index == 0) {
                    PaddingValues(bottom = 10.dp, start = 20.dp, end = 20.dp)
                } else {
                    PaddingValues(horizontal = 20.dp, vertical = 10.dp)
                }

                Row(
                    modifier = Modifier.padding(paddingValues),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                ) {
                    Text(
                        modifier = Modifier.widthIn(min = 26.dp),
                        text = "${index + 1}.",
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                        fontWeight = FontWeight.Medium,
                        textAlign = TextAlign.End,
                    )

                    Text(
                        text = topic.name.lowercase(),
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
            }
        }

    }
}

@Preview
@Composable
fun PreviewRoadmapScreen() {
    RoadmapsTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val uiState = RoadmapDetailUiState.Loading

            RoadmapScreen(uiState = uiState, onBack = { })
        }
    }
}
