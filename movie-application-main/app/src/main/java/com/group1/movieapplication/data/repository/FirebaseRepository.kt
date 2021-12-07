package com.group1.movieapplication.data.repository

import androidx.lifecycle.MutableLiveData
import com.group1.movieapplication.model.user.User
import com.group1.movieapplication.model.movieDetail.RatedMovie

interface FirebaseRepository {
    suspend fun saveRating(ratedMovie: RatedMovie)
    suspend fun getAllRating(movieId: String): MutableLiveData<RatedMovie>
    suspend fun getInfo(userId: String): MutableLiveData<User>
}
