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
import com.tripfellows.authorization.listeners.Router
import com.tripfellows.authorization.network.Account
import com.tripfellows.authorization.network.ApiRepo
import com.tripfellows.authorization.request.LoginRequest
import com.tripfellows.authorization.request.SignUpRequest
import com.tripfellows.client.MainActivity
import retrofit2.Call

import retrofit2.Callback

import retrofit2.Response


class AuthorizationActivity: AppCompatActivity(), Router {

    var fbAuth = FirebaseAuth.getInstance();

    override fun onCreate(savedInstanceState: Bundle?) {
        FirebaseApp.initializeApp(this)
        FirebaseAuth.getInstance().signOut()
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

    override fun mainMenu() {
        startActivity(Intent(this, MainActivity::class.java))
    }

    fun signUp(signUpRequest: SignUpRequest) {
        fbAuth.createUserWithEmailAndPassword(signUpRequest.email, signUpRequest.password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    ApiRepo.from(this).accountApi.createAccount(Account(signUpRequest.name, signUpRequest.phoneNumber))
                        .enqueue(object : Callback<Account> {
                            override fun onResponse(
                                call: Call<Account>,
                                response: Response<Account>
                            ) {
                                println(response.body())
                            }

                            override fun onFailure(call: Call<Account>, t: Throwable) {
                                println("Error")
                            }
                        })
                    startActivity(Intent(this, MainActivity::class.java))
                } else {
                    Toast.makeText(this, "Error: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }
}