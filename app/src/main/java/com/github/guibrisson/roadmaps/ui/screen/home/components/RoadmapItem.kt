package com.github.guibrisson.roadmaps.ui.screen.home.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.github.guibrisson.model.Roadmap
import com.github.guibrisson.roadmaps.ui.components.Favorite
import com.github.guibrisson.roadmaps.ui.theme.RoadmapsTheme


@Composable
fun RoadmapItem(
    modifier: Modifier = Modifier,
    roadmap: Roadmap,
    shape: Shape = RoundedCornerShape(8.dp),
    onRoadmap: (roadmapId: String) -> Unit,
    onFavorite: (roadmapId: String) -> Unit,
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(55.dp)
            .clip(shape)
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                shape = shape,
            )
            .background(MaterialTheme.colorScheme.surface)
            .clickable { onRoadmap(roadmap.id) }
            .padding(vertical = 12.dp, horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {

        Row(verticalAlignment = Alignment.CenterVertically) {
            Favorite(onFavorite = { onFavorite(roadmap.id) }, isFavorite = roadmap.isFavorite)

            Text(
                text = roadmap.name,
                style = MaterialTheme.typography.bodyMedium,
            )
        }

        //todo progress tracker

    }
}


@Preview
@Composable
private fun PreviewRoadmapItem() {
    RoadmapsTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val roadmap = Roadmap(
                id = "android",
                name = "Android",
                description = "How to became an Android Developer in 2024",
                isFavorite = false,
            )
            RoadmapItem(roadmap = roadmap, onRoadmap = { }, onFavorite = { })
        }
    }
}
