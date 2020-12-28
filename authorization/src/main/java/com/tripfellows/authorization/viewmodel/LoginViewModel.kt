package com.tripfellows.authorization.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.tripfellows.authorization.repo.AuthRepo
import com.tripfellows.authorization.request.LoginRequest
import com.tripfellows.authorization.states.RequestProgress
import com.tripfellows.authorization.states.ActionStatus

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val loginStatus: MediatorLiveData<ActionStatus> = MediatorLiveData()

    init {
        loginStatus.value = ActionStatus.NONE
    }

    fun getProgress(): LiveData<ActionStatus> {
        return loginStatus
    }

    fun login(loginRequest: LoginRequest) {
        loginStatus.postValue(ActionStatus.IN_PROGRESS)

        val progressLiveData = AuthRepo.getInstance(getApplication()).login(loginRequest)

        if (loginStatus.value != ActionStatus.IN_PROGRESS) {
            loginStatus.addSource(progressLiveData) {
                if (it == RequestProgress.SUCCESS) {
                    loginStatus.postValue(ActionStatus.SUCCESS)
                    loginStatus.removeSource(progressLiveData)
                } else if (it == RequestProgress.FAILED){
                    loginStatus.postValue(ActionStatus.FAILED)
                    loginStatus.removeSource(progressLiveData)
                }
            }
        }
    }
}