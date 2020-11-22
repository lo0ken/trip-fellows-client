package com.tripfellows.authorization.repo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.tripfellows.authorization.ApplicationModified
import com.tripfellows.authorization.network.ApiRepo
import com.tripfellows.authorization.network.request.CreateAccountRequest
import com.tripfellows.authorization.request.LoginRequest
import com.tripfellows.authorization.request.SignUpRequest
import com.tripfellows.authorization.states.Progress
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

    fun login(loginRequest: LoginRequest): LiveData<Progress> {
        val authProgress: MutableLiveData<Progress> = MutableLiveData()
        authProgress.value = Progress.IN_PROGRESS

        fbAuth.signInWithEmailAndPassword(loginRequest.email, loginRequest.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    authProgress.postValue(Progress.SUCCESS)
                } else {
                    authProgress.postValue(Progress.FAILED)
                }
            }
        return authProgress
    }

    fun signUp(signUpRequest: SignUpRequest): LiveData<Progress> {
        val signUpProgress: MutableLiveData<Progress> = MutableLiveData()
        signUpProgress.value = Progress.IN_PROGRESS

        fbAuth.createUserWithEmailAndPassword(signUpRequest.email, signUpRequest.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    createAccount(signUpRequest, signUpProgress)
                } else {
                    signUpProgress.postValue(Progress.FAILED)
                }
            }
        return signUpProgress
    }

    private fun createAccount(signUpRequest: SignUpRequest, signUpProgress: MutableLiveData<Progress>) {
        val accountToCreate =
            CreateAccountRequest(
                signUpRequest.name,
                signUpRequest.phoneNumber
            )

        apiRepo.accountApi.createAccount(accountToCreate).enqueue(object : Callback<CreateAccountRequest> {
            override fun onResponse(call: Call<CreateAccountRequest>, response: Response<CreateAccountRequest>) {
                if (response.isSuccessful && response.body() != null) {
                    signUpProgress.postValue(Progress.SUCCESS)
                }
            }

            override fun onFailure(call: Call<CreateAccountRequest>, t: Throwable) {
                fbAuth.currentUser?.delete()
                signUpProgress.postValue(Progress.FAILED)
            }
        })
    }
}