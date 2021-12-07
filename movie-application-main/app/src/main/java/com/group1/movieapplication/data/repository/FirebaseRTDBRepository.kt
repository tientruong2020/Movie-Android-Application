package com.group1.movieapplication.data.repository

import android.annotation.SuppressLint
import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import timber.log.Timber


class FirebaseRTDBRepository() {

    companion object {
        const val USER_TBL = "User"
        const val AVATAR_BUCKET_NAME = "Avatar"
        const val AVATAR_COLUMN_NAME = "avatar_uri"
        const val FIRST_NAME_COLUMN_NAME = "firstname"
        const val LAST_NAME_COLUMN_NAME = "lastname"
        const val UPLOAD_TAG = "upload_avatar"
        const val GET_RATED_MOVIE_TAG = "get_rated_movie"
        const val RATED_MOVIE_COLUMN_NAME = "RatedMovie"
    }

    var firebaseDatabase: FirebaseDatabase
    var userReference: DatabaseReference
    var storageReference: StorageReference
    var firebaseAuthRepository: FirebaseAuthRepository

    init {
        firebaseAuthRepository = FirebaseAuthRepository()
        firebaseDatabase = FirebaseDatabase.getInstance()
        userReference = firebaseDatabase.reference.child(USER_TBL)
        storageReference = FirebaseStorage.getInstance().getReference().child(AVATAR_BUCKET_NAME)
    }

    @SuppressLint("LogNotTimber")
    fun uploadUserAvatar(imgLocalUri: Uri): MutableLiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val fileName: String = System.currentTimeMillis().toString()
        val uid: String? = firebaseAuthRepository.getUID()
        if (uid != null) {
            val fileUploader: StorageReference = storageReference.child(uid).child(fileName)
            fileUploader.putFile(imgLocalUri).addOnSuccessListener {
                Log.d(UPLOAD_TAG, "Successfully upload Avatar: ${it.metadata?.path}")
                result.postValue(true)
                fileUploader.downloadUrl.addOnSuccessListener {
                    updateAvatarUriToDB(it.toString())
                }
            }
        }
        return result
    }

    @SuppressLint("LogNotTimber")
    fun updateAvatarUriToDB(dowloadAvatarUrl: String): Boolean {
        var isUpdated = false
        val uid: String? = firebaseAuthRepository.getUID()
        val userRef: DatabaseReference = userReference.child(uid!!)
        userRef.child(AVATAR_COLUMN_NAME).setValue(dowloadAvatarUrl).addOnSuccessListener {
            isUpdated = true
        }
        return isUpdated
    }

    fun updateUserInfo(firstname: String, lastname: String) {
        val uid: String? = firebaseAuthRepository.getUID()
        if (uid != null) {
            val userRef: DatabaseReference = userReference.child(uid)
            userRef.child(FIRST_NAME_COLUMN_NAME).setValue(firstname)
            userRef.child(LAST_NAME_COLUMN_NAME).setValue(lastname)
        } else {
            Timber.d("Can't Get Current User")
        }
    }

    fun getMyMovieID(): MutableLiveData<ArrayList<String>> {
        var movieIDList = ArrayList<String>()
        var movieIDMutableLiveData = MutableLiveData<ArrayList<String>>()
        val uid: String? = firebaseAuthRepository.getUID()
        uid?.let {
            val userRef = userReference.child(uid)
            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(userSnapshot: DataSnapshot) {
                    if (userSnapshot.hasChild(RATED_MOVIE_COLUMN_NAME)) {
                        val ratedMovieRef = userRef.child(RATED_MOVIE_COLUMN_NAME)
                        ratedMovieRef.addValueEventListener(object : ValueEventListener {
                            override fun onDataChange(dataSnapshot: DataSnapshot) {
                                val totalNumber = dataSnapshot.childrenCount
                                var count: Long = 0
                                for (movieIdSnapshot in dataSnapshot.children) {
                                    movieIDList.add(movieIdSnapshot.value.toString())
                                    count++
                                    if (count == totalNumber) {
                                        movieIDMutableLiveData.postValue(movieIDList)
                                    }
                                }
                            }

                            override fun onCancelled(error: DatabaseError) {
                                Timber.d(error.toException().toString())
                            }
                        })
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.d(error.toException().toString())
                }

            })
        }
        return movieIDMutableLiveData
    }

}