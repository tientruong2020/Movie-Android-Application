package com.group1.movieapplication.ui.profile

import android.text.TextUtils
import com.group1.movieapplication.model.user.User

object UpdateProfileValidation {

    fun isChangedProfileInfo(user: User, firstname: String, lastname: String): Boolean {
        if (!TextUtils.equals(user.firstname, firstname) || !TextUtils.equals(
                user.lastname,
                lastname
            )
        ) {
            return true
        }
        return false
    }

    fun isEmptyFields(firstname: String, lastname: String): Boolean {
        return (TextUtils.isEmpty(firstname) || TextUtils.isEmpty(lastname))
    }
}