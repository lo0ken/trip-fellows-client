package com.tripfellows.authorization.repo

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.tripfellows.authorization.ApplicationModified
import com.tripfellows.authorization.network.ApiRepo
import com.tripfellows.authorization.request.LoginRequest
import com.tripfellows.authorization.request.SignUpRequest
import com.tripfellows.authorization.states.AuthProgress

class AuthRepo(val apiRepo: ApiRepo) {

    companion object {
        fun getInstance(context: Context): AuthRepo {
            return ApplicationModified.from(context).authRepo
        }
    }

    var fbAuth = FirebaseAuth.getInstance();

    fun login(loginRequest: LoginRequest): LiveData<AuthProgress> {

        val authProgress: MutableLiveData<AuthProgress> = MutableLiveData()
        authProgress.value = AuthProgress.IN_PROGRESS

        fbAuth.signInWithEmailAndPassword(loginRequest.email, loginRequest.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    authProgress.postValue(AuthProgress.SUCCESS)
                } else {
                    authProgress.postValue(AuthProgress.FAILED)
                    "Error: ${task.exception?.message}"
                }
            }
        return authProgress;
    }

    fun signUp(signUpRequest: SignUpRequest) {

    }
}