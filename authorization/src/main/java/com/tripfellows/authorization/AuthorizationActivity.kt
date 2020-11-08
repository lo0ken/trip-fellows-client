package com.tripfellows.authorization

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.tripfellows.authorization.fragment.LoginFragment
import com.tripfellows.authorization.fragment.RegistrationFragment
import com.tripfellows.authorization.listeners.AuthorizationListener
import com.tripfellows.authorization.request.LoginRequest
import com.tripfellows.authorization.request.SignUpRequest
import com.tripfellows.client.MainActivity

class AuthorizationActivity: AppCompatActivity(), AuthorizationListener {

    var fbAuth = FirebaseAuth.getInstance();

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.authorization_activity)

        if (savedInstanceState == null) {
            val currentUser = fbAuth.currentUser
            if (currentUser != null) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                loadFragment(LoginFragment())
            }
        }
    }

    private fun loadFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.authorization_fragment_container, fragment)
            .commit()
    }

    override fun goToSignUp() {
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.authorization_fragment_container, RegistrationFragment())
            .addToBackStack(null)
            .commit()
    }

    override fun signIn(loginRequest: LoginRequest) {
        fbAuth.signInWithEmailAndPassword(loginRequest.email, loginRequest.password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    override fun signUp(signUpRequest: SignUpRequest) {
        fbAuth.createUserWithEmailAndPassword(signUpRequest.email, signUpRequest.password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}