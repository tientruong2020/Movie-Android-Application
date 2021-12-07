package com.group1.movieapplication.model.movieDetail

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IMDBTrailerResponse(
    @SerialName("videoId")
    val videoUrl: String
) {
}