package com.group1.movieapplication.model.movieDetail

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Movie(
    @SerialName("title")
    var title: String,
    @SerialName("type")
    var type: String,
    @SerialName("year")
    var year: String,
    @SerialName("plot")
    var summary: String,
    @SerialName("genres")
    var genre: String,
    @SerialName("imDbRating")
    var rating: String
) {


    companion object {
        lateinit var movieId: String
    }
}

