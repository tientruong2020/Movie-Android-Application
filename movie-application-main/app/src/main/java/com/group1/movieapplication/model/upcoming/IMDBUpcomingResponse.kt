package com.group1.movieapplication.model.upcoming


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class IMDBUpcomingResponse(
    @SerialName("errorMessage")
    val errorMessage: String,
    @SerialName("items")
    val upComingItems: List<UpComingItem>
)