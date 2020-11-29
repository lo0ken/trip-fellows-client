package com.tripfellows.authorization

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.tripfellows.authorization.fragment.LoginFragment
import com.tripfellows.authorization.fragment.RegistrationFragment
import com.tripfellows.authorization.listeners.AuthRouter


class AuthorizationActivity: AppCompatActivity(), AuthRouter {

    private val fbAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        FirebaseAuth.getInstance().signOut()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.authorization_activity)

        if (savedInstanceState == null) {
            val currentUser = fbAuth.currentUser
            if (currentUser != null) {
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("USER_UID", currentUser.uid)
                startActivity(intent)
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

    override fun mainMenu() {
        startActivity(Intent(this, MainActivity::class.java))
    }
}