package com.tripfellows.authorization.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.tripfellows.authorization.repo.AuthRepo
import com.tripfellows.authorization.request.SignUpRequest
import com.tripfellows.authorization.states.RequestProgress
import com.tripfellows.authorization.states.ActionState

class RegistrationViewModel(application: Application) : AndroidViewModel(application) {
    private val signUpState: MediatorLiveData<ActionState> = MediatorLiveData()

    init {
        signUpState.value = ActionState.NONE
    }

    fun getProgress(): LiveData<ActionState> {
        return signUpState
    }

    fun signUp(signUpRequest: SignUpRequest) {
        signUpState.postValue(ActionState.IN_PROGRESS)

        val progressLiveData = AuthRepo.getInstance(getApplication()).signUp(signUpRequest)

        if (signUpState.value != ActionState.IN_PROGRESS) {
            signUpState.addSource(progressLiveData) {
                if (it == RequestProgress.SUCCESS) {
                    signUpState.postValue(ActionState.SUCCESS)
                    signUpState.removeSource(progressLiveData)
                } else if (it == RequestProgress.FAILED) {
                    signUpState.postValue(ActionState.FAILED)
                    signUpState.removeSource(progressLiveData)
                }
            }
        }
    }
}