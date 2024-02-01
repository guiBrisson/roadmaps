package com.github.guibrisson.model


sealed class TopicSystem(
    open val id: String,
    open val name: String,
    open val content: String,
)

data class TopicFolder(
    override val id: String,
    override val name: String,
    override val content: String,
    val topics: List<TopicSystem>,
    val topicsAmount: Int,
    var progress: List<String> = emptyList(),
) : TopicSystem(id, name, content)

data class TopicItem(
    override val id: String,
    override val name: String,
    override val content: String,
) : TopicSystem(id, name, content)
