package com.group1.movieapplication.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.group1.movieapplication.data.repository.SearchTVShowRepository
import com.group1.movieapplication.model.search.TVSearchResponse

class SearchViewModel : ViewModel() {
    private val searchTVShowRepository: SearchTVShowRepository

    fun getSearchTVShowRepository(query: String): LiveData<TVSearchResponse?> {
        return searchTVShowRepository.getSearchTVShowRepository(query)
    }

    init {
        searchTVShowRepository = SearchTVShowRepository()
    }
}