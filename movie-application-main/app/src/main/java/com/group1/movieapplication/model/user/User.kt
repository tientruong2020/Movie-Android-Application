package com.group1.movieapplication.model.user

import com.google.firebase.database.IgnoreExtraProperties
import java.io.Serializable

@IgnoreExtraProperties
class User : Serializable {
    var firstname: String? = null
    var lastname: String? = null
    var email: String? = null
    var ratedMovie: String? = null
    var avatar_uri: String? = null

    constructor() {

    }

    constructor(firstname: String?, lastname: String?, email: String?, ratedMovie: String?) {
        this.firstname = firstname
        this.lastname = lastname
        this.email = email
        this.ratedMovie = ratedMovie
    }

    constructor(firstname: String?, lastname: String?, imageUrl: String?) {
        this.firstname = firstname
        this.lastname = lastname
        this.avatar_uri = imageUrl
    }
}
