package com.group1.movieapplication.model.upcoming


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UpComingItem(
    @SerialName("contentRating")
    val contentRating: String,
    @SerialName("directorList")
    val directorList: List<Director>,
    @SerialName("directors")
    val directors: String,
    @SerialName("fullTitle")
    val fullTitle: String,
    @SerialName("genreList")
    val genreList: List<Genre>,
    @SerialName("genres")
    val genres: String,
    @SerialName("id")
    val id: String,
    @SerialName("imDbRating")
    val imDbRating: String,
    @SerialName("imDbRatingCount")
    val imDbRatingCount: String,
    @SerialName("image")
    val image: String,
    @SerialName("metacriticRating")
    val metacriticRating: String,
    @SerialName("plot")
    val plot: String,
    @SerialName("releaseState")
    val releaseState: String,
    @SerialName("runtimeMins")
    val runtimeMins: String,
    @SerialName("runtimeStr")
    val runtimeStr: String,
    @SerialName("starList")
    val starList: List<Star>,
    @SerialName("stars")
    val stars: String,
    @SerialName("title")
    val title: String,
    @SerialName("year")
    val year: String
)