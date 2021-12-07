package com.group1.movieapplication.data.repository

import com.group1.movieapplication.model.popular.IMDBPopularResponse
import com.group1.movieapplication.model.movieDetail.IMDBMovieResponse
import com.group1.movieapplication.model.movieDetail.IMDBTrailerResponse
import com.group1.movieapplication.model.upcoming.IMDBUpcomingResponse
import com.group1.movieapplication.utils.NetworkResult

interface IMDBRepository {
    suspend fun getPopularMovies(): NetworkResult<IMDBPopularResponse>
    suspend fun getPopularTVs(): NetworkResult<IMDBPopularResponse>
    suspend fun getUpcoming(): NetworkResult<IMDBUpcomingResponse>
    suspend fun getMovieTrailer(id: String): IMDBTrailerResponse
    suspend fun getMovieById(id: String): IMDBMovieResponse
}