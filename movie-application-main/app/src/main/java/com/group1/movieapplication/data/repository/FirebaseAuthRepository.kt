package com.group1.movieapplication.data.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.group1.movieapplication.model.movieDetail.Movie
import com.group1.movieapplication.model.movieDetail.RatedMovie
import com.group1.movieapplication.model.user.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class FirebaseAuthRepository() : FirebaseRepository {
    companion object {
        const val USER_TBL = "User"
    }

    var firebaseAuth: FirebaseAuth
    var firebaseDatabase: FirebaseDatabase
    var userRef: DatabaseReference

    init {
        firebaseAuth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        userRef = firebaseDatabase.reference.child(USER_TBL)
    }

    fun isLogined(): Boolean {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            return true
        }
        return false
    }

    fun getUID(): String? {
        val currentUser = firebaseAuth.currentUser
        if (currentUser != null) {
            return currentUser.uid
        }
        return null
    }


    fun getUserInfo(): MutableLiveData<User> {
        val currentUser = firebaseAuth.currentUser
        val id = currentUser?.uid
        val userRef: DatabaseReference = firebaseDatabase.reference.child(USER_TBL).child(id!!)
        var mutableLiveDataUser = MutableLiveData<User>()
        userRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mutableLiveDataUser.value = snapshot.getValue(User::class.java)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.d("Error", error.message)
            }

        })
        return mutableLiveDataUser
    }

    override fun saveRating(ratedMovie: RatedMovie): MutableLiveData<Boolean> {
        val result = MutableLiveData<Boolean>()
        val hashMap = HashMap<String, Any>()
        hashMap.put(Movie.movieId, Movie.movieId)

        firebaseDatabase.reference.child(USER_TBL).child(ratedMovie.userId!!).child("RatedMovie")
            .updateChildren(hashMap)


        firebaseDatabase.reference.child("RatedMovie").child(Movie.movieId)
            .child(ratedMovie.userId!!).child("ratingScore").setValue(ratedMovie.ratingScore)


        firebaseDatabase.reference.child("RatedMovie").child(Movie.movieId)
            .child(ratedMovie.userId!!).child("comment").setValue(ratedMovie.comment)
            .addOnCompleteListener {
                result.value = it.isSuccessful

            }
        return result
    }

        override suspend fun getAllRating(movieId: String): MutableLiveData<ArrayList<RatedMovie>> {
            val userRatingListLiveData = MutableLiveData<ArrayList<RatedMovie>>()
            val ratingRef: DatabaseReference =
                firebaseDatabase.reference.child("RatedMovie").child(movieId)

            Timber.d(movieId)

            ratingRef.addValueEventListener(object : ValueEventListener {

                override fun onDataChange(snapshot: DataSnapshot) {
                    val userRating = ArrayList<RatedMovie>()
                    if (snapshot.exists()) {
                        userRating.clear()
                        Timber.d("start getting ratings")

                        for (dataSnapshot: DataSnapshot in snapshot.children) {

                            val userId: String = dataSnapshot.key.toString()

                            val ratingScore = dataSnapshot.child("ratingScore").value.toString()

                            val comment = dataSnapshot.child("comment").value.toString()


                            userRef.child(userId)
                                .addValueEventListener(object : ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val user: User? = snapshot.getValue(User::class.java)
                                        user?.let {
                                            userRating.add(
                                                RatedMovie(
                                                    userId,
                                                    "${it.firstname} ${it.lastname}",
                                                    it.avatar_uri,
                                                    ratingScore,
                                                    comment
                                                )
                                            )
                                            userRatingListLiveData.value = userRating
                                        }
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        Timber.d(error.toString())
                                    }

                                })

                        }
                    } else {
                        Timber.d("movie not rated")
                        ratingRef.onDisconnect()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.d(error.toString())
                }
            })
            return userRatingListLiveData
        }

        override suspend fun getInfo(userId: String): MutableLiveData<User> {
            val userRef: DatabaseReference =
                firebaseDatabase.reference.child("User").child(userId)
            val user = MutableLiveData<User>()

            userRef.addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(userSnapshot: DataSnapshot) {
                    val firstname = userSnapshot.child("firstname").value.toString()
                    val lastname = userSnapshot.child("lastname").value.toString()
                    val imageUrl = userSnapshot.child("avatar_uri").value.toString()

                    user.value = User(firstname, lastname, imageUrl)
                }

                override fun onCancelled(error: DatabaseError) {
                    Timber.d(error.toString())
                }

            })
            return user
        }

        fun logout() {
            firebaseAuth.signOut()
        }

        fun changePassword(currentPW: String, newPW: String): MutableLiveData<Boolean> {
            val currentUser = firebaseAuth.currentUser
            var result = MutableLiveData<Boolean>()
            if (currentUser != null) {
                val credential: AuthCredential =
                    EmailAuthProvider.getCredential(currentUser.email!!, currentPW)
                currentUser.reauthenticate(credential).addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        currentUser.updatePassword(newPW).addOnCompleteListener {
                            result.value = it.isSuccessful
                            logout()
                        }
                    }
                }
            }
            return result
        }
    }
