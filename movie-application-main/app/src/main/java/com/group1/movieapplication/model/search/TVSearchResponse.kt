package com.group1.movieapplication.model.search

data class TVSearchResponse(
    val errorMessage: String,
    val expression: String,
    val results: List<Result>,
    val searchType: String
) {
    fun getSearchitem(): List<Result> {
        return results
    }
}