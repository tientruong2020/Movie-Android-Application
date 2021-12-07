package com.group1.movieapplication.data.remote

import javax.inject.Inject

class IMDBDataSource @Inject constructor(
    private val service: IMDBService
) {
    suspend fun getPopularMovies() = service.getPopularMovies()

    suspend fun getPopularTVs() = service.getPopularTVs()

    suspend fun getUpcoming() = service.getUpcomingMovies()

    suspend fun getMovieTrailer(id: String) = service.getMovieTrailer(id)

    suspend fun getMovieById(id: String) = service.getMovieById(id)
}