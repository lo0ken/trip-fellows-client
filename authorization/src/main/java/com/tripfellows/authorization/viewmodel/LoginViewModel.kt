package com.tripfellows.authorization.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.tripfellows.authorization.repo.AuthRepo
import com.tripfellows.authorization.request.LoginRequest
import com.tripfellows.authorization.states.AuthProgress
import com.tripfellows.authorization.states.LoginState

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val loginState: MediatorLiveData<LoginState> = MediatorLiveData()

    init {
        loginState.value = LoginState.NONE
    }

    fun getProgress(): LiveData<LoginState> {
        return loginState
    }

    fun login(loginRequest: LoginRequest) {
        loginState.postValue(LoginState.IN_PROGRESS)

        val progressLiveData = AuthRepo.getInstance(getApplication()).login(loginRequest)


        if (loginState.value != LoginState.IN_PROGRESS) {
            loginState.addSource(progressLiveData) {
                if (it == AuthProgress.SUCCESS) {
                    loginState.postValue(LoginState.SUCCESS)
                    loginState.removeSource(progressLiveData)
                } else if (it == AuthProgress.FAILED){
                    loginState.postValue(LoginState.FAILED)
                    loginState.removeSource(progressLiveData)
                }
            }
        }

    }
}