package com.group1.movieapplication.model.upcoming


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Genre(
    @SerialName("key")
    val key: String,
    @SerialName("value")
    val value: String
)