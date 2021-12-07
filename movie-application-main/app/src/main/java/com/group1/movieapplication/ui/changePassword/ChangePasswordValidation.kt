package com.group1.movieapplication.ui.changePassword

import android.text.TextUtils

object ChangePasswordValidation {

    fun isConfirmPwEqualNewPw(newPW: String, confirmPW: String): Boolean {
        return TextUtils.equals(newPW, confirmPW)
    }

    fun notEmptyAllFields(currentPW: String, newPW: String, confirmPW: String): Boolean {
        return !(TextUtils.isEmpty(currentPW) || TextUtils.isEmpty(newPW) || TextUtils.isEmpty(
            confirmPW
        ))
    }
}