package com.group1.movieapplication.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.movieapplication.data.repository.FirebaseAuthRepository
import com.group1.movieapplication.data.repository.FirebaseRTDBRepository
import com.group1.movieapplication.data.repository.IMDBRepository
import com.group1.movieapplication.model.user.User
import com.group1.movieapplication.model.movieDetail.IMDBMovieResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject


@HiltViewModel
class ProfileViewModel @Inject constructor(val imdbRepository: IMDBRepository) : ViewModel() {
    val firebaseAuthRepository = FirebaseAuthRepository()
    val firebaseRTDBRepository = FirebaseRTDBRepository()
    val movieList = MutableLiveData<ArrayList<IMDBMovieResponse>>()
    var ratedMovieIDList = MutableLiveData<ArrayList<String>>()


    fun isLogined() {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseAuthRepository.isLogined()
        }
    }

    fun getUserInfo(): MutableLiveData<User> {
        return firebaseAuthRepository.getUserInfo()
    }


    fun getRatedMovieIdList(): MutableLiveData<ArrayList<String>> {
        return firebaseRTDBRepository.getMyMovieID()
    }

    fun getMovieImgList(movieIdList: ArrayList<String>) {
        viewModelScope.launch {
            val content = ArrayList<IMDBMovieResponse>()
            try {
                coroutineScope {
                    movieIdList.forEach { id ->
                        val apiCaller = async { imdbRepository.getMovieById(id) }
                        val response = apiCaller.await()
                        content.add(response)
                        movieList.postValue(content)
                    }
                }
            } catch (e: Exception) {
                Timber.d(e)
            }
        }
    }
}