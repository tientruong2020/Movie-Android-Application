package com.group1.movieapplication.data.remote

import com.group1.movieapplication.model.search.TVSearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("SearchMovie/k_t1wmg05t/{title}")
    fun getSearchTVShow(
        @Path(
            value = "title",
            encoded = true
        ) title: String?
    ): Call<TVSearchResponse?>?
}