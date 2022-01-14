package com.group1.movieapplication.model.movieDetail

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
class RatedMovie : Serializable {

    var userId: String? = null
    var movieId: String? = null
    var ratingScore: String? = null
    var comment: String? = null
    var username: String? = null
    var userImage: String? = null

    constructor(userId: String?, ratingScore: String?, comment: String?) {
        this.userId = userId
        this.ratingScore = ratingScore
        this.comment = comment
    }

    constructor(userId: String?,username: String?, userImage: String?, ratingScore: String?, comment: String?) {
        this.userId = userId
        this.username = username
        this.userImage = userImage
        this.ratingScore = ratingScore
        this.comment = comment
    }

}