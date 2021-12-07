package com.group1.movieapplication.ui.view

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.TextUtils
import android.util.Base64
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FacebookAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.group1.movieapplication.R
import com.group1.movieapplication.ui.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.activity_register.view.*
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

class LoginActivity : AppCompatActivity() {

    lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    var callbackManager: CallbackManager? = null

    companion object {
        private const val RC_SIGN_IN = 120
        private const val TAG = "GOOGLE_SIGN_IN_TAG"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        auth = FirebaseAuth.getInstance()
        callbackManager = CallbackManager.Factory.create()
        loginfbButton.setReadPermissions("email")
        loginfbButton.setOnClickListener {
            siginin()
        }

        val currentuser = auth.currentUser
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        auth = FirebaseAuth.getInstance()
        checkUser()
        if (currentuser != null) {
            Firebase.auth.signOut()
        }
        login()
    }

    private fun checkUser() {
        val currentuser = auth.currentUser
        if (currentuser != null) {
            Firebase.auth.signOut()
        }

    }

    private fun login() {

        loginButton.setOnClickListener {

            if (TextUtils.isEmpty(usernameInput.text.toString())) {
                usernameInput.setError("Please enter username")
                return@setOnClickListener
            } else if (TextUtils.isEmpty(passwordInput.text.toString())) {
                usernameInput.setError("Please enter password")
                return@setOnClickListener
            }
            auth.signInWithEmailAndPassword(
                usernameInput.text.toString(),
                passwordInput.text.toString()
            )
                .addOnCompleteListener {
                    if (it.isSuccessful) {
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    } else {
                        Toast.makeText(
                            this@LoginActivity,
                            "Login failed, please try again! ",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

        }
        loginggButton.setOnClickListener {
            Log.d(TAG, "onCreate: begin Google signIn")
            val intent = googleSignInClient.signInIntent
            startActivityForResult(intent, RC_SIGN_IN)
        }

        registerText.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        callbackManager?.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            Log.d(TAG, "onActivityResult : Google Sign In intent result")
            val accountTask = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = accountTask.getResult(ApiException::class.java)
                firebaseAuthWithGoogleAccount(account)
            } catch (e: Exception) {
                Log.d(TAG, "onActivityResult: ${e.message}")
            }
        }
    }

    private fun firebaseAuthWithGoogleAccount(account: GoogleSignInAccount?) {
        Log.d(TAG, "firebaseAuthWithGoogleAccount: begin firebase auth with google account ")

        val credential = GoogleAuthProvider.getCredential(account!!.idToken, null)
        auth.signInWithCredential(credential)
            .addOnSuccessListener { authResult ->

                Log.d(TAG, "firebaseAuthWithGoogleAccount: LoggedIn")

                val currentuser = auth.currentUser

                val uid = currentuser!!.uid
                val email = currentuser!!.email

                Log.d(TAG, "firebaseAuthWithGoogleAccount: Uid: $uid")
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Email: $email")

                if (authResult.additionalUserInfo!!.isNewUser) {
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: Account created...\n$email")
                    Toast.makeText(
                        this@LoginActivity,
                        "Account created...\n$email",
                        Toast.LENGTH_SHORT
                    ).show()
                } else {
                    Log.d(TAG, "firebaseAuthWithGoogleAccount: Existing user...\n$email")
                    Toast.makeText(this@LoginActivity, "LoggedIn...\n$email", Toast.LENGTH_SHORT)
                        .show()
                }
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Log.d(TAG, "firebaseAuthWithGoogleAccount: Loggin Failed due to ${e.message}")
                Toast.makeText(
                    this@LoginActivity,
                    "Loggin Failed due to ${e.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun siginin() {
        loginfbButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult?) {
                handleFaceBookAccessToken(result!!.accessToken)
            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException?) {

            }
        })
    }

    private fun handleFaceBookAccessToken(accessToken: AccessToken?) {
        val credentials = FacebookAuthProvider.getCredential(accessToken!!.token)
        auth!!.signInWithCredential(credentials)
            .addOnSuccessListener { result ->
                val email = result.user?.email
                Toast.makeText(this, "You logged in with " + email, Toast.LENGTH_LONG).show()
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, e.message, Toast.LENGTH_LONG).show()
            }
    }

    private fun printkeyHask() {
        try {
            val info = packageManager.getPackageInfo(
                "com.group1.movieapplication",
                PackageManager.GET_SIGNATURES
            )
            for (signature in info.signatures) {
                val md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray())
                Log.e("KEYHASK", Base64.encodeToString(md.digest(), Base64.DEFAULT))
            }
        } catch (e: PackageManager.NameNotFoundException) {

        } catch (e: NoSuchAlgorithmException) {

        }
    }
}

