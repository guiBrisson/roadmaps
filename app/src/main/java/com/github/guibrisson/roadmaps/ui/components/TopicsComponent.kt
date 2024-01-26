package com.github.guibrisson.roadmaps.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.guibrisson.model.TopicFolder
import com.github.guibrisson.model.TopicItem
import com.github.guibrisson.model.TopicSystem
import com.github.guibrisson.roadmaps.R
import com.github.guibrisson.roadmaps.ui.theme.RoadmapsTheme

fun LazyListScope.topicContent(topic: TopicSystem) {
    item {
        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = topic.name.lowercase(),
            style = MaterialTheme.typography.titleSmall,
        )
    }

    //todo: support for markdown
    item {
        if (topic.content.isNotEmpty()) {
            Text(
                modifier = Modifier.padding(top = 12.dp, start = 20.dp, end = 20.dp),
                text = topic.content,
                style = MaterialTheme.typography.bodyMedium.copy(lineHeight = 20.sp),
            )
        }
    }
}

fun LazyListScope.topics(
    detailId: String,
    topics: List<TopicSystem>,
    onFolder: (Array<String>) -> Unit,
    onItem: (Array<String>) -> Unit,
) {
    itemsIndexed(topics) { index, topic ->
        val interactionSource = remember { MutableInteractionSource() }
        val paddingValues = if (index == 0) {
            PaddingValues(bottom = 10.dp, start = 20.dp, end = 20.dp)
        } else {
            PaddingValues(horizontal = 20.dp, vertical = 10.dp)
        }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(paddingValues)
                .clickable(
                    interactionSource = interactionSource,
                    indication = null,
                    onClick = {
                        val navigationArgs: Array<String> = arrayOf(detailId, topic.id)

                        when (topic) {
                            is TopicFolder -> onFolder(navigationArgs)
                            is TopicItem -> onItem(navigationArgs)
                        }
                    }
                ),
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                modifier = Modifier.widthIn(min = 26.dp),
                text = "${index + 1}.",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodyMedium,
            )

            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = R.drawable.ic_minus),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
            )

            Text(
                modifier = Modifier.weight(1f, fill = false),
                text = topic.name.lowercase().replace(" ", "_"),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
            )

            if (topic is TopicFolder) {
                val size = topic.topics.size
                Text(
                    modifier = Modifier,
                    text = "[$size]",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                )
            }
        }
    }
}

@Preview
@Composable
fun PreviewTopicContent() {
    RoadmapsTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val topic = TopicItem(id = "1", name = "topic", content = "# topic\n**epic** topic")
            LazyColumn {
                topicContent(topic)
            }
        }
    }
}


@Preview
@Composable
fun PreviewTopics() {
    RoadmapsTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val topics = listOf(
                TopicFolder(
                    id = "1",
                    name = "FolderFolderFolderFolderFolderFolder",
                    content = "",
                    topics = emptyList(),
                ),
                TopicItem(
                    id = "2",
                    name = "Item",
                    content = "",
                )
            )

            LazyColumn {
                topics(
                    detailId = "preview",
                    topics = topics,
                    onFolder = { },
                    onItem = { },
                )
            }
        }
    }
}
