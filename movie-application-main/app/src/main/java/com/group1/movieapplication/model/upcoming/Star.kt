package com.group1.movieapplication.model.upcoming


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Star(
    @SerialName("id")
    val id: String,
    @SerialName("name")
    val name: String
)