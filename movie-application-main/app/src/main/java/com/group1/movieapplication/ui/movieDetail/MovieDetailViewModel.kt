package com.group1.movieapplication.ui.movieDetail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.movieapplication.data.repository.FirebaseAuthRepository
import com.group1.movieapplication.data.repository.IMDBRepository
import com.group1.movieapplication.model.user.User
import com.group1.movieapplication.model.movieDetail.IMDBMovieResponse
import com.group1.movieapplication.model.movieDetail.IMDBTrailerResponse
import com.group1.movieapplication.model.movieDetail.Movie
import com.group1.movieapplication.model.movieDetail.RatedMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val repository: IMDBRepository,
) : ViewModel() {

    private val firebaseRepo = FirebaseAuthRepository()

    private var _movie = MutableLiveData<IMDBMovieResponse?>(null)
    val movie: LiveData<IMDBMovieResponse?> get() = _movie

    private var _trailer = MutableLiveData<IMDBTrailerResponse?>(null)
    val trailer: LiveData<IMDBTrailerResponse?> get() = _trailer

    private var _rating = MutableLiveData<RatedMovie>()
    val rating: LiveData<RatedMovie?> get() = _rating

    private var _user = MutableLiveData<User>()
    val user: LiveData<User?> get() = _user


    fun getMovie() {
        viewModelScope.launch {
            _movie.value = repository.getMovieById(Movie.movieId)
        }
    }

    fun getTrailer() {
        viewModelScope.launch {
            _trailer.value = repository.getMovieTrailer(Movie.movieId)
            Timber.d(Movie.movieId)
        }
    }

    fun saveRating(ratedMovie: RatedMovie) {
        viewModelScope.launch {
            firebaseRepo.saveRating(ratedMovie)
        }
    }

    fun getAllRating(movieId: String) {
        viewModelScope.launch {
            _rating = firebaseRepo.getAllRating(movieId)
        }
    }

    fun getInfo(userId: String) {
        viewModelScope.launch {
            _user = firebaseRepo.getInfo(userId)
        }
    }
}