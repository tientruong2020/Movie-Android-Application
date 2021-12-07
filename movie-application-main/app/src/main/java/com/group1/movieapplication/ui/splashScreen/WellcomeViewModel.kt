package com.group1.movieapplication.ui.splashScreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import com.group1.movieapplication.data.repository.FirebaseAuthRepository

class WellcomeViewModel(application: Application) : AndroidViewModel(application) {
    var firebaseAuthRepository: FirebaseAuthRepository

    init {
        firebaseAuthRepository = FirebaseAuthRepository()
    }

    fun isLogined(): Boolean {
        return firebaseAuthRepository.isLogined()
    }
}