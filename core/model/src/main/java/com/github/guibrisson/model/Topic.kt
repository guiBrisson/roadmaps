package com.github.guibrisson.model


sealed class Topic(
    open val id: String,
    open val name: String,
    open val content: String,
)

data class TopicFolder(
    override val id: String,
    override val name: String,
    override val content: String,
    val topics: List<Topic>,
) : Topic(id, name, content)

data class TopicItem(
    override val id: String,
    override val name: String,
    override val content: String,
) : Topic(id, name, content)
