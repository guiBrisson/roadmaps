package com.github.guibrisson.model

data class RoadmapDetail(
    val id: String,
    val name: String,
    val description: String,
    var isFavorite: Boolean = false,
    val content: TopicFolder,
)
