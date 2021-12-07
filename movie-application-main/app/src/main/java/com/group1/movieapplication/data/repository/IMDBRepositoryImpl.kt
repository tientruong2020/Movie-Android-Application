package com.group1.movieapplication.data.repository

import com.group1.movieapplication.data.remote.IMDBDataSource
import com.group1.movieapplication.model.popular.IMDBPopularResponse
import com.group1.movieapplication.model.movieDetail.IMDBMovieResponse
import com.group1.movieapplication.model.movieDetail.IMDBTrailerResponse
import com.group1.movieapplication.model.upcoming.IMDBUpcomingResponse
import com.group1.movieapplication.utils.BaseApiResponse
import com.group1.movieapplication.utils.NetworkResult
import javax.inject.Inject

class IMDBRepositoryImpl @Inject constructor(
    private val dataSource: IMDBDataSource
) : IMDBRepository, BaseApiResponse() {
    override suspend fun getPopularMovies(): NetworkResult<IMDBPopularResponse> {
        return safeApiCall {
            dataSource.getPopularMovies()
        }
    }

    override suspend fun getPopularTVs(): NetworkResult<IMDBPopularResponse> {
        return safeApiCall {
            dataSource.getPopularTVs()
        }
    }

    override suspend fun getUpcoming(): NetworkResult<IMDBUpcomingResponse> {
        return safeApiCall {
            dataSource.getUpcoming()
        }
    }

    override suspend fun getMovieTrailer(id: String): IMDBTrailerResponse {
        return dataSource.getMovieTrailer(id)
    }

    override suspend fun getMovieById(id: String): IMDBMovieResponse {
        return dataSource.getMovieById(id)
    }
}