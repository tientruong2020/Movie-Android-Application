package com.group1.movieapplication.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.google.android.material.navigation.NavigationView
import com.group1.movieapplication.R
import com.group1.movieapplication.databinding.ActivityMainBinding
import com.group1.movieapplication.ui.changePassword.ChangePasswordDialog
import com.group1.movieapplication.ui.view.LoginActivity
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainViewModel: MainViewModel
    private lateinit var toggle: ActionBarDrawerToggle

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var nav_view: NavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setViewModel()
        initDrawerNavigation()
        setContentView(binding.root)
        setNavigation()
    }

    private fun setNavigation() {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        NavigationUI.setupWithNavController(
            binding.bottomNavigationView,
            navHostFragment.navController
        )
    }

    fun initDrawerNavigation() {
        drawerLayout = binding.drawerLayout
        nav_view = binding.navView
        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.open_drawer_nav_title,
            R.string.close_drawer_nav_title
        )
        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setDataForNavDrawer()

        nav_view.setNavigationItemSelectedListener {
            when (it.itemId) {
                (R.id.item_change_password) -> {
                    openChangePasswordDialog()
                }
                (R.id.item_logout) -> {
                    mainViewModel.signout()
                    checkLogined()
                }
            }
            true
        }
    }

    fun setDataForNavDrawer() {
        // mapping component
        val headerView: View = nav_view.getHeaderView(0)
        val avatarImageView: CircleImageView = headerView.findViewById(R.id.nav_imv_avatar)
        val fullnameTextView: TextView = headerView.findViewById(R.id.nav_tw_fullname)
        val emailTextView: TextView = headerView.findViewById(R.id.nav_tw_email)

        mainViewModel.getUserInfo().observe(this, Observer { user ->
            fullnameTextView.text = user.firstname + " " + user.lastname
            emailTextView.text = user.email
            if (user.avatar_uri == null || user.avatar_uri == "") {
                avatarImageView.setImageResource(R.drawable.defaultavatar)
            } else {
                val imgUri: Uri = Uri.parse(user.avatar_uri)
                Picasso.get().load(imgUri).into(avatarImageView)
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.fragment_container_view).navigateUp()
    }

    fun setViewModel() {
        mainViewModel = ViewModelProvider(this).get(MainViewModel::class.java)
    }

    fun checkLogined() {
        val isLogined = mainViewModel.isLogined()
        if (!isLogined) {
            toLoginActivity()
        }
    }

    fun toLoginActivity() {
        val intent = Intent(this, LoginActivity::class.java)
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    fun openChangePasswordDialog() {
        val changePasswordDialog = ChangePasswordDialog()
        supportFragmentManager?.let {
            changePasswordDialog.show(it, ChangePasswordDialog::class.java.name)
        }
    }

}

