package com.group1.movieapplication.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.group1.movieapplication.data.repository.FirebaseAuthRepository
import com.group1.movieapplication.model.user.User

class MainViewModel(application: Application) : AndroidViewModel(application) {

    var firebaseAuthRepository: FirebaseAuthRepository

    init {
        firebaseAuthRepository = FirebaseAuthRepository()
    }

    fun isLogined(): Boolean {
        return firebaseAuthRepository.isLogined()
    }

    fun signout() {
        firebaseAuthRepository.logout()
    }

    fun getUserInfo(): MutableLiveData<User> {
        return firebaseAuthRepository.getUserInfo()
    }
}