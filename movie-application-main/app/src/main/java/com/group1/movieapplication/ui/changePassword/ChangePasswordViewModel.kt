package com.group1.movieapplication.ui.changePassword

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.group1.movieapplication.data.repository.FirebaseAuthRepository
import kotlinx.coroutines.launch

class ChangePasswordViewModel(application: Application) : AndroidViewModel(application) {

    var firebaseAuthRepository: FirebaseAuthRepository

    init {
        firebaseAuthRepository = FirebaseAuthRepository()
    }

    fun changePassword(currentPW: String, newPW: String): MutableLiveData<Boolean> {
        return firebaseAuthRepository.changePassword(currentPW, newPW)
    }

    fun signout() {
        viewModelScope.launch {
            firebaseAuthRepository.logout()
        }
    }
}