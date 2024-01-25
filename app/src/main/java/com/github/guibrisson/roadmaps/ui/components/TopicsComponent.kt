package com.github.guibrisson.roadmaps.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.github.guibrisson.model.TopicFolder
import com.github.guibrisson.model.TopicItem
import com.github.guibrisson.model.TopicSystem
import com.github.guibrisson.roadmaps.R

fun LazyListScope.topicsComponent(
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
                text = topic.name.lowercase().replace(" ", "_"),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
            )

            if (topic is TopicFolder) {
                val size = topic.topics.size
                Text(
                    text = "[$size]",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.1f),
                )
            }
        }
    }
}