package com.github.guibrisson.roadmaps.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.guibrisson.model.TopicFolder
import com.github.guibrisson.model.TopicItem
import com.github.guibrisson.model.TopicSystem
import com.github.guibrisson.roadmaps.R
import com.github.guibrisson.roadmaps.ui.theme.RoadmapsTheme
import com.github.guibrisson.roadmaps.ui.theme.blue
import com.github.guibrisson.roadmaps.ui.theme.green
import com.halilibo.richtext.markdown.Markdown
import com.halilibo.richtext.ui.CodeBlockStyle
import com.halilibo.richtext.ui.RichTextStyle
import com.halilibo.richtext.ui.material3.RichText
import com.halilibo.richtext.ui.resolveDefaults
import com.halilibo.richtext.ui.string.RichTextStringStyle

fun LazyListScope.topicContent(
    topic: TopicSystem,
    onMarkDone: (() -> Unit)? = null,
) {
    item {
        Text(
            modifier = Modifier.padding(horizontal = 20.dp),
            text = topic.name.lowercase(),
            style = MaterialTheme.typography.titleSmall,
        )
    }

    item {
        val colorScheme = MaterialTheme.colorScheme

        val richTextStyle by remember {
            mutableStateOf(
                RichTextStyle(
                    codeBlockStyle = CodeBlockStyle(
                        wordWrap = true,
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(4.dp))
                            .background(colorScheme.onBackground.copy(alpha = 0.05f))
                            .padding(horizontal = 12.dp)
                    ),
                    stringStyle = RichTextStringStyle(
                        codeStyle = SpanStyle(background = colorScheme.onBackground.copy(0.1f)),
                        linkStyle = SpanStyle(
                            color = blue,
                            textDecoration = TextDecoration.Underline,
                        )
                    )
                ).resolveDefaults()
            )
        }

        if (topic.content.isNotEmpty()) {
            ProvideTextStyle(TextStyle(lineHeight = 20.sp, fontSize = 14.sp)) {
                RichText(
                    modifier = Modifier.padding(top = 12.dp, start = 20.dp, end = 20.dp),
                    style = richTextStyle
                ) {
                    Markdown(content = topic.content)
                }
            }
        }
    }

    if (topic is TopicItem) {
        item {
            MarkDone(
                modifier = Modifier.padding(start = 20.dp, top = 28.dp),
                isDone = topic.isDone,
                onMarkDone = { onMarkDone?.let { it() } },
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
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
                fontWeight = FontWeight.Medium,
                textAlign = TextAlign.End,
                style = MaterialTheme.typography.bodyMedium,
            )

            val (painter, tint, contentDescription) = calculateIconProperties(topic)

            Icon(
                modifier = Modifier.size(16.dp),
                painter = painter,
                contentDescription = contentDescription,
                tint = tint,
            )

            Text(
                modifier = Modifier.weight(1f, fill = false),
                text = topic.name.lowercase(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium,
            )

            if (topic is TopicFolder) {
                val progress = topic.progressAmount
                val amount = topic.topicsAmount
                Text(
                    modifier = Modifier,
                    text = "[${progress}/${amount}]",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
                )
            }
        }
    }
}

private data class IconProperties(
    val painter: Painter,
    val tint: Color,
    val contentDescription: String,
)

@Composable
private fun calculateIconProperties(topic: TopicSystem): IconProperties {
    val check = IconProperties(
        painterResource(id = R.drawable.ic_check),
        green,
        stringResource(id = R.string.topic_check_icon_content_description),
    )

    val dot = IconProperties(
        painterResource(id = R.drawable.ic_dot),
        blue,
        stringResource(id = R.string.topic_minus_icon_content_description),
    )

    val minus = IconProperties(
        painterResource(id = R.drawable.ic_minus),
        MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
        stringResource(id = R.string.topic_dot_icon_content_description),
    )

    return when (topic) {
        is TopicFolder -> {
            when (topic.progressAmount) {
                0 -> minus
                topic.topicsAmount -> check
                else -> dot
            }
        }

        is TopicItem -> {
            if (topic.isDone) check else minus
        }
    }
}

@Preview
@Composable
fun PreviewTopicContent() {
    RoadmapsTheme {
        Surface(color = MaterialTheme.colorScheme.background) {
            val topic = TopicItem(
                id = "1",
                name = "topic",
                content = contentMarkdownExample,
                isDone = false,
            )
            LazyColumn {
                topicContent(topic, onMarkDone = { })
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
                    topicsAmount = 4,
                    progressAmount = 1,
                ),
                TopicItem(
                    id = "2",
                    name = "Item",
                    content = "",
                    isDone = true,
                ),
                TopicItem(
                    id = "3",
                    name = "Item",
                    content = "",
                    isDone = false,
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

private val contentMarkdownExample = """
    ---
    # h1 Heading 8-)
    ## h2 Heading
    ### h3 Heading
    #### h4 Heading
    ##### h5 Heading
    ###### h6 Heading

    ## Lists

    1. Lorem ipsum dolor sit amet
    2. Consectetur adipiscing elit
    3. Integer molestie lorem at massa

    ## Code
    Inline `code`
    Syntax highlighting

    ``` js
    var foo = function (bar) {
      return bar++;
    };

    console.log(foo(5));
    ```
""".trimIndent()
