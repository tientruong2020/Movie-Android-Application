package com.group1.movieapplication.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.group1.movieapplication.data.repository.IMDBRepository
import com.group1.movieapplication.model.popular.IMDBPopularResponse
import com.group1.movieapplication.model.popular.PopularItem
import com.group1.movieapplication.model.upcoming.IMDBUpcomingResponse
import com.group1.movieapplication.model.upcoming.UpComingItem
import com.group1.movieapplication.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: IMDBRepository
) : ViewModel() {
    private var _movieList = MutableLiveData<NetworkResult<IMDBPopularResponse>?>(null)
    val movieList: LiveData<NetworkResult<IMDBPopularResponse>?> get() = _movieList

    private var _tvList = MutableLiveData<NetworkResult<IMDBPopularResponse>?>(null)
    val tvList: LiveData<NetworkResult<IMDBPopularResponse>?> get() = _tvList

    private var _upcomingList = MutableLiveData<NetworkResult<IMDBUpcomingResponse>?>(null)
    val upComingList: LiveData<NetworkResult<IMDBUpcomingResponse>?> get() = _upcomingList

    var selectedPopularItem: PopularItem? = null
    var selectedUpComingItem: UpComingItem? = null

    fun fetchData() {
        getMovieList()
        getTVList()
        getUpcoming()
    }

    private fun getMovieList() {
        viewModelScope.launch(Dispatchers.Main) {
            _movieList.value = repository.getPopularMovies()
        }
    }

    private fun getTVList() {
        viewModelScope.launch(Dispatchers.Main) {
            _tvList.value = repository.getPopularTVs()
        }
    }

    private fun getUpcoming() {
        viewModelScope.launch(Dispatchers.Main) {
            _upcomingList.value = repository.getUpcoming()
        }
    }

}