package com.group1.movieapplication.model.movieDetail

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MovieImg(
    @SerialName("id")
    var id: String,
    @SerialName("image")
    var image: String,
    @SerialName("title")
    var title: String
) {
}