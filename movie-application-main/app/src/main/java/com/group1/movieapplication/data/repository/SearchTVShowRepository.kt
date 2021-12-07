package com.group1.movieapplication.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.group1.movieapplication.data.remote.ApiService
import com.group1.movieapplication.di.ApiClient
import com.group1.movieapplication.model.search.TVSearchResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SearchTVShowRepository {
    private val apiService: ApiService

    fun getSearchTVShowRepository(query: String): LiveData<TVSearchResponse?> {
        val data: MutableLiveData<TVSearchResponse?> = MutableLiveData<TVSearchResponse?>()
        apiService.getSearchTVShow(query)?.enqueue(object : Callback<TVSearchResponse?> {
            override fun onResponse(
                call: Call<TVSearchResponse?>,
                response: Response<TVSearchResponse?>
            ) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<TVSearchResponse?>, t: Throwable) {
                data.value = null
            }

        })
        return data
    }

    init {
        apiService = ApiClient.apiClient.create(ApiService::class.java)
    }
}