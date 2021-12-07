package com.group1.movieapplication.ui.profile

import android.app.Activity
import android.app.AlertDialog
import com.group1.movieapplication.R

class LoadingDialog(val mActivity: Activity) {
    private lateinit var alertDialog: AlertDialog

    fun startLoading() {
        val infalter = mActivity.layoutInflater
        val dialogView = infalter.inflate(R.layout.progress_dialog_layout, null)

        /**set Dialog*/
        val bulider = AlertDialog.Builder(mActivity)
        bulider.setView(dialogView)
        bulider.setCancelable(true)
        alertDialog = bulider.create()
        alertDialog.show()
    }

    fun dismissDialog() {
        alertDialog.dismiss()
    }
}