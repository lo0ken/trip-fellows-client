package com.tripfellows.authorization

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.tripfellows.authorization.fragment.LoginFragment
import com.tripfellows.authorization.fragment.RegistrationFragment
import com.tripfellows.authorization.listeners.SignUpListener

class AuthorizationActivity: AppCompatActivity(), SignUpListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.authorization_activity)

        if (savedInstanceState == null) {
            loadFragment(LoginFragment())
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
}