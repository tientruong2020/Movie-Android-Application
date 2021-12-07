package com.group1.movieapplication.data.remote

import com.group1.movieapplication.model.popular.IMDBPopularResponse
import com.group1.movieapplication.model.movieDetail.IMDBMovieResponse
import com.group1.movieapplication.model.movieDetail.IMDBTrailerResponse
import com.group1.movieapplication.model.upcoming.IMDBUpcomingResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface IMDBService {
    companion object {
        const val API_KEY = "k_ghdf4evm"
    }


    @GET("MostPopularMovies/${API_KEY}")
    suspend fun getPopularMovies(): Response<IMDBPopularResponse>


    @GET("MostPopularTVs/${API_KEY}")
    suspend fun getPopularTVs(): Response<IMDBPopularResponse>


    @GET("ComingSoon/${API_KEY}")
    suspend fun getUpcomingMovies(): Response<IMDBUpcomingResponse>

    @GET("YoutubeTrailer/${API_KEY}/{id}")
    suspend fun getMovieTrailer(@Path(value = "id", encoded = true) id: String): IMDBTrailerResponse

    @GET("Title/${API_KEY}/{id}")
    suspend fun getMovieById(@Path(value = "id", encoded = true) id: String): IMDBMovieResponse


}