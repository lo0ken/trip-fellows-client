package com.tripfellows.authorization.repo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.tripfellows.authorization.ApplicationModified
import com.tripfellows.authorization.network.Account
import com.tripfellows.authorization.network.ApiRepo
import com.tripfellows.authorization.request.LoginRequest
import com.tripfellows.authorization.request.SignUpRequest
import com.tripfellows.authorization.states.AuthProgress
import com.tripfellows.authorization.states.SignUpProgress
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AuthRepo(private val apiRepo: ApiRepo) {

    companion object {
        fun getInstance(context: Context): AuthRepo {
            return ApplicationModified.from(context).authRepo
        }
    }

    var fbAuth = FirebaseAuth.getInstance()

    fun login(loginRequest: LoginRequest): LiveData<AuthProgress> {
        val authProgress: MutableLiveData<AuthProgress> = MutableLiveData()
        authProgress.value = AuthProgress.IN_PROGRESS

        fbAuth.signInWithEmailAndPassword(loginRequest.email, loginRequest.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    authProgress.postValue(AuthProgress.SUCCESS)
                } else {
                    authProgress.postValue(AuthProgress.FAILED)
                }
            }
        return authProgress
    }

    fun signUp(signUpRequest: SignUpRequest): LiveData<SignUpProgress> {
        val signUpProgress: MutableLiveData<SignUpProgress> = MutableLiveData()
        signUpProgress.value = SignUpProgress.IN_PROGRESS

        fbAuth.createUserWithEmailAndPassword(signUpRequest.email, signUpRequest.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    createAccount(signUpRequest, signUpProgress)
                } else {
                    signUpProgress.postValue(SignUpProgress.FAILED)
                }
            }

        return signUpProgress
    }

    private fun createAccount(signUpRequest: SignUpRequest, signUpProgress: MutableLiveData<SignUpProgress>) {
        val accountToCreate = Account(signUpRequest.name, signUpRequest.phoneNumber)

        apiRepo.accountApi.createAccount(accountToCreate).enqueue(object : Callback<Account> {
            override fun onResponse(call: Call<Account>, response: Response<Account>) {
                if (response.isSuccessful && response.body() != null) {
                    signUpProgress.postValue(SignUpProgress.SUCCESS)
                }
            }

            override fun onFailure(call: Call<Account>, t: Throwable) {
                fbAuth.currentUser?.delete()
                signUpProgress.postValue(SignUpProgress.FAILED)
            }
        })
    }
}