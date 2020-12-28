package com.tripfellows.authorization

import android.content.Intent
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.tripfellows.authorization.fragment.LoginFragment
import com.tripfellows.authorization.fragment.NoConnectionFragment
import com.tripfellows.authorization.fragment.RegistrationFragment
import com.tripfellows.authorization.listeners.AuthRouter
import com.tripfellows.authorization.listeners.ConnectionRouter


class AuthorizationActivity: AppCompatActivity(), AuthRouter, ConnectionRouter {

    private val fbAuth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.authorization_activity)

        if (!isOnline()) {
            loadFragment(NoConnectionFragment())
            return
        }

        if (savedInstanceState == null) {
            authenticateUser()
        }
    }

    private fun isOnline(): Boolean {
        val connectivityManager = getSystemService(CONNECTIVITY_SERVICE) as ConnectivityManager

        val network: Network? = connectivityManager.activeNetwork
        val capabilities : NetworkCapabilities = connectivityManager.getNetworkCapabilities(network)
            ?: return false

        return capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) || capabilities.hasTransport(
            NetworkCapabilities.TRANSPORT_CELLULAR)
    }

    override fun reload() {
        if (isOnline()) {
            supportFragmentManager.popBackStack()
            authenticateUser()
        }
    }

    private fun authenticateUser() {
        val currentUser = fbAuth.currentUser
        if (currentUser != null) {
            val intent = Intent(this, MainActivity::class.java)
            intent.putExtra("USER_UID", currentUser.uid)
            startActivity(intent)
        } else {
            loadFragment(LoginFragment())
        }
    }

    override fun hasInternetConnection(): Boolean {
        val isOnline: Boolean = isOnline()
        if (!isOnline) {
            loadFragment(NoConnectionFragment())
        }
        return isOnline
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