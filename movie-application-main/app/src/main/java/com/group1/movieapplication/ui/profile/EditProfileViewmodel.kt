package com.group1.movieapplication.ui.profile

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.group1.movieapplication.data.repository.FirebaseAuthRepository
import com.group1.movieapplication.data.repository.FirebaseRTDBRepository
import com.group1.movieapplication.model.user.User

class EditProfileViewmodel(application: Application) : AndroidViewModel(application) {

    var firebaseRTDBRepository: FirebaseRTDBRepository
    var firebaseAuthRepository: FirebaseAuthRepository

    init {
        firebaseRTDBRepository = FirebaseRTDBRepository()
        firebaseAuthRepository = FirebaseAuthRepository()
    }

    fun uploadAvatar(localImgUri: Uri): MutableLiveData<Boolean> {
        return firebaseRTDBRepository.uploadUserAvatar(localImgUri)
    }

    fun getUserInfor(): MutableLiveData<User> {
        return firebaseAuthRepository.getUserInfo()
    }

    fun updateProfile(firstname: String, lastname: String) {
        firebaseRTDBRepository.updateUserInfo(firstname, lastname)
    }
}