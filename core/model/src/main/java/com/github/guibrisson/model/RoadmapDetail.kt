package com.github.guibrisson.model

data class RoadmapDetail(
    val id: String,
    val description: String,
    val topics: List<Topic>,
)
