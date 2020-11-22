package com.tripfellows.authorization.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.tripfellows.authorization.repo.AuthRepo
import com.tripfellows.authorization.request.LoginRequest
import com.tripfellows.authorization.states.Progress
import com.tripfellows.authorization.states.State

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val loginState: MediatorLiveData<State> = MediatorLiveData()

    init {
        loginState.value = State.NONE
    }

    fun getProgress(): LiveData<State> {
        return loginState
    }

    fun login(loginRequest: LoginRequest) {
        loginState.postValue(State.IN_PROGRESS)

        val progressLiveData = AuthRepo.getInstance(getApplication()).login(loginRequest)

        if (loginState.value != State.IN_PROGRESS) {
            loginState.addSource(progressLiveData) {
                if (it == Progress.SUCCESS) {
                    loginState.postValue(State.SUCCESS)
                    loginState.removeSource(progressLiveData)
                } else if (it == Progress.FAILED){
                    loginState.postValue(State.FAILED)
                    loginState.removeSource(progressLiveData)
                }
            }
        }
    }
}