package com.group1.movieapplication.data.repository

import androidx.lifecycle.MutableLiveData
import com.group1.movieapplication.model.user.User
import com.group1.movieapplication.model.movieDetail.RatedMovie

interface FirebaseRepository {
    fun saveRating(ratedMovie: RatedMovie):MutableLiveData<Boolean>
    suspend fun getAllRating(movieId: String): MutableLiveData<ArrayList<RatedMovie>>
    suspend fun getInfo(userId: String): MutableLiveData<User>
}
