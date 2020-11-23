package com.tripfellows.authorization.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.tripfellows.authorization.repo.AuthRepo
import com.tripfellows.authorization.request.LoginRequest
import com.tripfellows.authorization.states.RequestProgress
import com.tripfellows.authorization.states.ActionState

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val loginState: MediatorLiveData<ActionState> = MediatorLiveData()

    init {
        loginState.value = ActionState.NONE
    }

    fun getProgress(): LiveData<ActionState> {
        return loginState
    }

    fun login(loginRequest: LoginRequest) {
        loginState.postValue(ActionState.IN_PROGRESS)

        val progressLiveData = AuthRepo.getInstance(getApplication()).login(loginRequest)

        if (loginState.value != ActionState.IN_PROGRESS) {
            loginState.addSource(progressLiveData) {
                if (it == RequestProgress.SUCCESS) {
                    loginState.postValue(ActionState.SUCCESS)
                    loginState.removeSource(progressLiveData)
                } else if (it == RequestProgress.FAILED){
                    loginState.postValue(ActionState.FAILED)
                    loginState.removeSource(progressLiveData)
                }
            }
        }
    }
}