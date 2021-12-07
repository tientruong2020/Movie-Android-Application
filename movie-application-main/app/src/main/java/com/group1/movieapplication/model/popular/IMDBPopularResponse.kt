package com.group1.movieapplication.model.popular


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IMDBPopularResponse(
    @SerialName("errorMessage")
    val errorMessage: String,
    @SerialName("items")
    val popularItems: List<PopularItem>
)