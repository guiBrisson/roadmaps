package com.github.guibrisson.roadmaps.ui.screen.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.github.guibrisson.roadmaps.R
import com.github.guibrisson.roadmaps.ui.components.alignLastItemToBottom
import com.github.guibrisson.roadmaps.ui.components.loading
import com.github.guibrisson.roadmaps.ui.components.failure
import com.github.guibrisson.roadmaps.ui.screen.home.components.RoadmapItem
import com.github.guibrisson.roadmaps.ui.theme.RoadmapsTheme

@Composable
fun HomeRoute(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel(),
    onRoadmap: (roadmapId: String) -> Unit,
) {
    val uiState by viewModel.roadmapUiState.collectAsStateWithLifecycle()

    LaunchedEffect(Unit) {
        viewModel.fetchAllRoadmaps()
    }

    HomeScreen(modifier = modifier, uiState = uiState, onRoadmap = onRoadmap)
}


@Composable
internal fun HomeScreen(
    modifier: Modifier = Modifier,
    uiState: RoadmapsUiState,
    onRoadmap: (roadmapId: String) -> Unit,
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp),
        verticalArrangement = alignLastItemToBottom()
    ) {
        item {
            Row(
                modifier = Modifier.padding(top = 40.dp, bottom = 24.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp),
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_roadmap),
                    contentDescription = stringResource(id = R.string.roadmap_icon_content_description)
                )

                Text(
                    text = stringResource(id = R.string.app_name),
                    style = MaterialTheme.typography.titleMedium,
                )
            }
        }

        when (uiState) {
            is RoadmapsUiState.Success -> roadmapsSuccess(uiState, onRoadmap)
            RoadmapsUiState.Loading -> loading()
            is RoadmapsUiState.Failure -> failure(errorMessage = uiState.errorMessage)
        }
    }
}

private fun LazyListScope.roadmapsSuccess(
    uiState: RoadmapsUiState.Success,
    onRoadmap: (roadmapId: String) -> Unit,
) {
    item {
        Text(
            modifier = Modifier.padding(bottom = 14.dp),
            text = stringResource(id = R.string.all_roadmaps),
            style = MaterialTheme.typography.bodyLarge,
        )
    }

    items(uiState.roadmaps) { roadmap ->
        RoadmapItem(
            modifier = Modifier.padding(vertical = 6.dp),
            roadmap = roadmap,
            onRoadmap = onRoadmap,
        )
    }

    item { Spacer(modifier = Modifier.padding(1.dp)) }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    RoadmapsTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val uiState = RoadmapsUiState.Loading
            HomeScreen(uiState = uiState, onRoadmap = { })
        }
    }
}
