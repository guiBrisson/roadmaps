package com.github.guibrisson.model

data class Roadmap(
    val id: String,
    val name: String,
    val description: String,
    val isFavorite: Boolean = false,
)
