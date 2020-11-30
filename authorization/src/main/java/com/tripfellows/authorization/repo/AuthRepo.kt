package com.tripfellows.authorization.repo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.auth.FirebaseAuth
import com.tripfellows.authorization.ApplicationModified
import com.tripfellows.authorization.model.Account
import com.tripfellows.authorization.network.ApiRepo
import com.tripfellows.authorization.network.request.CreateAccountRequest
import com.tripfellows.authorization.request.LoginRequest
import com.tripfellows.authorization.request.SignUpRequest
import com.tripfellows.authorization.states.RequestProgress
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AuthRepo(private val apiRepo: ApiRepo) {

    companion object {
        fun getInstance(context: Context): AuthRepo {
            return ApplicationModified.from(context).authRepo
        }
    }

    private val account: MutableLiveData<Account> = MutableLiveData()

    fun getAccount(): MutableLiveData<Account> {
        return account
    }


    var fbAuth = FirebaseAuth.getInstance()

    fun login(loginRequest: LoginRequest): LiveData<RequestProgress> {
        val authProgress: MutableLiveData<RequestProgress> = MutableLiveData()
        authProgress.value = RequestProgress.IN_PROGRESS

        fbAuth.signInWithEmailAndPassword(loginRequest.email, loginRequest.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    authProgress.postValue(RequestProgress.SUCCESS)
                } else {
                    authProgress.postValue(RequestProgress.FAILED)
                }
            }
        return authProgress
    }

    fun signUp(signUpRequest: SignUpRequest): LiveData<RequestProgress> {
        val signUpProgress: MutableLiveData<RequestProgress> = MutableLiveData()
        signUpProgress.value = RequestProgress.IN_PROGRESS

        fbAuth.createUserWithEmailAndPassword(signUpRequest.email, signUpRequest.password)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    createAccount(signUpRequest, signUpProgress)
                } else {
                    signUpProgress.postValue(RequestProgress.FAILED)
                }
            }
        return signUpProgress
    }

    private fun createAccount(signUpRequest: SignUpRequest, signUpProgress: MutableLiveData<RequestProgress>) {
        val accountToCreate =
            CreateAccountRequest(
                signUpRequest.name,
                signUpRequest.phoneNumber
            )

        apiRepo.accountApi.createAccount(accountToCreate).enqueue(object : Callback<CreateAccountRequest> {
            override fun onResponse(call: Call<CreateAccountRequest>, response: Response<CreateAccountRequest>) {
                if (response.isSuccessful && response.body() != null) {
                    signUpProgress.postValue(RequestProgress.SUCCESS)
                }
            }

            override fun onFailure(call: Call<CreateAccountRequest>, t: Throwable) {
                fbAuth.currentUser?.delete()
                signUpProgress.postValue(RequestProgress.FAILED)
            }
        })
    }

    fun getCurrentAccount() {
        apiRepo.accountApi.getCurrentAccount().enqueue(object: Callback<Account> {
            override fun onResponse(call: Call<Account>, response: Response<Account>) {
                if (response.isSuccessful && response.body() != null) {
                    account.postValue(response.body())
                }
            }

            override fun onFailure(call: Call<Account>, t: Throwable) {
            }
        })
    }
}