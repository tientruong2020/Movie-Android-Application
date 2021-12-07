package com.group1.movieapplication.ui.splashScreen

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.WindowInsets
import android.view.WindowManager
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.group1.movieapplication.R
import com.group1.movieapplication.ui.MainActivity
import com.group1.movieapplication.ui.view.LoginActivity
import kotlinx.android.synthetic.main.activity_wellcome.*

class WellcomeActivity : AppCompatActivity() {

    lateinit var logoAnimation: Animation
    lateinit var bannerAnimation: Animation
    lateinit var wellcomeViewModel: WellcomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        wellcomeViewModel = ViewModelProvider(this).get(WellcomeViewModel::class.java)
        setFullScreen()
        setContentView(R.layout.activity_wellcome)
        initAnimation()
        checkInternet()
    }

    @Suppress("DEPRECATION")
    fun setFullScreen() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.decorView.windowInsetsController!!.hide(
                WindowInsets.Type.statusBars()
            )
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
    }

    fun initAnimation() {
        logoAnimation = AnimationUtils.loadAnimation(this, R.anim.logo_animation)
        bannerAnimation = AnimationUtils.loadAnimation(this, R.anim.banner_animation)
        imv_logo.animation = logoAnimation
        tw_banner.animation = bannerAnimation
        tw_group_name.animation = bannerAnimation
    }

    fun transScreen() {
        Handler(Looper.getMainLooper()).postDelayed({
            checkLogined()
        }, 4000)
    }

    private fun checkInternet() {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val isConnected = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            val activeNetwork = connectivityManager.activeNetworkInfo
            activeNetwork?.isConnectedOrConnecting == true
        } else {
            val networkCapabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
            networkCapabilities?.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) == true
        }
        if (isConnected) {
            transScreen()
        } else {
            showErrorDialog()
        }
    }

    private fun showErrorDialog() {
        val dialogBuilder = AlertDialog.Builder(this)
            .setTitle("Loading Failed")
            .setMessage("App need internet connection to continue")
            .setPositiveButton("OK") { _, _ ->
                finishAndRemoveTask()
            }

        dialogBuilder.create().show()
    }

    fun checkLogined() {
        val isLogined = wellcomeViewModel.isLogined()
        if (isLogined) {
            toHomeActivity()
        } else {
            toLoginActivity()
        }
    }

    fun toHomeActivity() {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun toLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

}
