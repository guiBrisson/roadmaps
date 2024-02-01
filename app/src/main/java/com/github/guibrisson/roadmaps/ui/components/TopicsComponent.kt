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
import androidx.compose.ui.res.painterResource
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

            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = R.drawable.ic_minus),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.2f),
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
                    topicsAmount = 1,
                    progressAmount = 1,
                ),
                TopicItem(
                    id = "2",
                    name = "Item",
                    content = "",
                    isDone = true,
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
    __Advertisement :)__

    - __[pica](https://nodeca.github.io/pica/demo/)__ - high quality and fast image
      resize in browser.
    - __[babelfish](https://github.com/nodeca/babelfish/)__ - developer friendly
      i18n with plurals support and easy syntax.

    You will like those projects!

    ---

    # h1 Heading 8-)
    ## h2 Heading
    ### h3 Heading
    #### h4 Heading
    ##### h5 Heading
    ###### h6 Heading


    ## Horizontal Rules

    ___

    ---

    ***


    ## Typographic replacements

    Enable typographer option to see result.

    (c) (C) (r) (R) (tm) (TM) (p) (P) +-

    test.. test... test..... test?..... test!....

    !!!!!! ???? ,,  -- ---

    "Smartypants, double quotes" and 'single quotes'


    ## Emphasis

    **This is bold text**

    __This is bold text__

    *This is italic text*

    _This is italic text_

    ~~Strikethrough~~


    ## Blockquotes


    > Blockquotes can also be nested...
    >> ...by using additional greater-than signs right next to each other...
    > > > ...or with spaces between arrows.


    ## Lists

    Unordered

    + Create a list by starting a line with `+`, `-`, or `*`
    + Sub-lists are made by indenting 2 spaces:
      - Marker character change forces new list start:
        * Ac tristique libero volutpat at
        + Facilisis in pretium nisl aliquet
        - Nulla volutpat aliquam velit
    + Very easy!

    Ordered

    1. Lorem ipsum dolor sit amet
    2. Consectetur adipiscing elit
    3. Integer molestie lorem at massa


    1. You can use sequential numbers...
    1. ...or keep all the numbers as `1.`

    Start numbering with offset:

    57. foo
    1. bar


    ## Code

    Inline `code`

    Indented code

        // Some comments
        line 1 of code
        line 2 of code
        line 3 of code


    Block code "fences"

    ```
    Sample text here...
    ```

    Syntax highlighting

    ``` js
    var foo = function (bar) {
      return bar++;
    };

    console.log(foo(5));
    ```

    ## Tables

    | Option | Description |
    | ------ | ----------- |
    | data   | path to data files to supply the data that will be passed into templates. |
    | engine | engine to be used for processing templates. Handlebars is the default. |
    | ext    | extension to be used for dest files. |

    Right aligned columns

    | Option | Description |
    | ------:| -----------:|
    | data   | path to data files to supply the data that will be passed into templates. |
    | engine | engine to be used for processing templates. Handlebars is the default. |
    | ext    | extension to be used for dest files. |


    ## Links

    [link text](http://dev.nodeca.com)

    [link with title](http://nodeca.github.io/pica/demo/ "title text!")

    Autoconverted link https://github.com/nodeca/pica (enable linkify to see)


    ## Images

    ![Minion](https://octodex.github.com/images/minion.png)
    ![Stormtroopocat](https://octodex.github.com/images/stormtroopocat.jpg "The Stormtroopocat")

    Like links, Images also have a footnote style syntax

    ![Alt text][id]

    With a reference later in the document defining the URL location:

    [id]: https://octodex.github.com/images/dojocat.jpg  "The Dojocat"


    ## Plugins

    The killer feature of `markdown-it` is very effective support of
    [syntax plugins](https://www.npmjs.org/browse/keyword/markdown-it-plugin).


    ### [Emojies](https://github.com/markdown-it/markdown-it-emoji)

    > Classic markup: :wink: :cry: :laughing: :yum:
    >
    > Shortcuts (emoticons): :-) :-( 8-) ;)

    see [how to change output](https://github.com/markdown-it/markdown-it-emoji#change-output) with twemoji.


    ### [Subscript](https://github.com/markdown-it/markdown-it-sub) / [Superscript](https://github.com/markdown-it/markdown-it-sup)

    - 19^th^
    - H~2~O


    ### [\<ins>](https://github.com/markdown-it/markdown-it-ins)

    ++Inserted text++


    ### [\<mark>](https://github.com/markdown-it/markdown-it-mark)

    ==Marked text==


    ### [Footnotes](https://github.com/markdown-it/markdown-it-footnote)

    Footnote 1 link[^first].

    Footnote 2 link[^second].

    Inline footnote^[Text of inline footnote] definition.

    Duplicated footnote reference[^second].

    [^first]: Footnote **can have markup**

        and multiple paragraphs.

    [^second]: Footnote text.


    ### [Definition lists](https://github.com/markdown-it/markdown-it-deflist)

    Term 1

    :   Definition 1
    with lazy continuation.

    Term 2 with *inline markup*

    :   Definition 2

            { some code, part of Definition 2 }

        Third paragraph of definition 2.

    _Compact style:_

    Term 1
      ~ Definition 1

    Term 2
      ~ Definition 2a
      ~ Definition 2b


    ### [Abbreviations](https://github.com/markdown-it/markdown-it-abbr)

    This is HTML abbreviation example.

    It converts "HTML", but keep intact partial entries like "xxxHTMLyyy" and so on.

    *[HTML]: Hyper Text Markup Language

    ### [Custom containers](https://github.com/markdown-it/markdown-it-container)

    ::: warning
    *here be dragons*
    :::
""".trimIndent()
