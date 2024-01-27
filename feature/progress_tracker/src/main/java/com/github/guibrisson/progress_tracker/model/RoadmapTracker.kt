package com.github.guibrisson.progress_tracker.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RoadmapTracker(
    @SerialName("roadmap_id") val roadmapId: String,
    var progress: List<String>,
    @SerialName("is_favorite") var isFavorite: Boolean,
)
