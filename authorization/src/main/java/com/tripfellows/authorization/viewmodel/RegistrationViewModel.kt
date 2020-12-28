package com.tripfellows.authorization.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.tripfellows.authorization.repo.AuthRepo
import com.tripfellows.authorization.request.SignUpRequest
import com.tripfellows.authorization.states.RequestProgress
import com.tripfellows.authorization.states.ActionStatus

class RegistrationViewModel(application: Application) : AndroidViewModel(application) {
    private val signUpStatus: MediatorLiveData<ActionStatus> = MediatorLiveData()

    init {
        signUpStatus.value = ActionStatus.NONE
    }

    fun getProgress(): LiveData<ActionStatus> {
        return signUpStatus
    }

    fun signUp(signUpRequest: SignUpRequest) {
        signUpStatus.postValue(ActionStatus.IN_PROGRESS)

        val progressLiveData = AuthRepo.getInstance(getApplication()).signUp(signUpRequest)

        if (signUpStatus.value != ActionStatus.IN_PROGRESS) {
            signUpStatus.addSource(progressLiveData) {
                if (it == RequestProgress.SUCCESS) {
                    signUpStatus.postValue(ActionStatus.SUCCESS)
                    signUpStatus.removeSource(progressLiveData)
                } else if (it == RequestProgress.FAILED) {
                    signUpStatus.postValue(ActionStatus.FAILED)
                    signUpStatus.removeSource(progressLiveData)
                }
            }
        }
    }
}