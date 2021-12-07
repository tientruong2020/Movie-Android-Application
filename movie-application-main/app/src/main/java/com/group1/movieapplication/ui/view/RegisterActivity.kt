package com.group1.movieapplication.ui.view

import android.os.Bundle
import android.text.TextUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.group1.movieapplication.R
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    lateinit var auth: FirebaseAuth
    var databaseReference: DatabaseReference? = null
    var database: FirebaseDatabase? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()
        databaseReference = database?.reference!!.child("User")

        register()
    }

    private fun register() {


        registerButton.setOnClickListener {

            if (TextUtils.isEmpty(firstnameInput.text.toString())) {
                firstnameInput.setError("Please enter first name ")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(lastnameInput.text.toString())) {
                firstnameInput.setError("Please enter last name ")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(usernameInput.text.toString())) {
                firstnameInput.setError("Please enter user name ")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(passwordInput.text.toString())) {
                firstnameInput.setError("Please enter password ")
                return@setOnClickListener
            }


            auth.createUserWithEmailAndPassword(
                usernameInput.text.toString(),
                passwordInput.text.toString()
            )
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        val currentUser = auth.currentUser
                        val currentUSerDb = databaseReference?.child((currentUser?.uid!!))
                        currentUSerDb?.child("firstname")?.setValue(firstnameInput.text.toString())
                        currentUSerDb?.child("lastname")?.setValue(lastnameInput.text.toString())
                        currentUSerDb?.child("email")?.setValue(usernameInput.text.toString())
                        currentUSerDb?.child("avatar_uri")?.setValue("")

                        Toast.makeText(
                            this@RegisterActivity,
                            "Register Success. ",
                            Toast.LENGTH_LONG
                        ).show()
                        finish()

                    } else {
                        Toast.makeText(
                            this@RegisterActivity,
                            "Register Fail: " + (it.exception?.message ?: 0),
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
        }
    }
}