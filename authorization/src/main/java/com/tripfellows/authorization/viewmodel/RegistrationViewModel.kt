package com.tripfellows.authorization.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.tripfellows.authorization.repo.AuthRepo
import com.tripfellows.authorization.request.SignUpRequest
import com.tripfellows.authorization.states.Progress
import com.tripfellows.authorization.states.State

class RegistrationViewModel(application: Application) : AndroidViewModel(application) {
    private val signUpState: MediatorLiveData<State> = MediatorLiveData()

    init {
        signUpState.value = State.NONE
    }

    fun getProgress(): LiveData<State> {
        return signUpState
    }

    fun signUp(signUpRequest: SignUpRequest) {
        signUpState.postValue(State.IN_PROGRESS)

        val progressLiveData = AuthRepo.getInstance(getApplication()).signUp(signUpRequest)

        if (signUpState.value != State.IN_PROGRESS) {
            signUpState.addSource(progressLiveData) {
                if (it == Progress.SUCCESS) {
                    signUpState.postValue(State.SUCCESS)
                    signUpState.removeSource(progressLiveData)
                } else if (it == Progress.FAILED) {
                    signUpState.postValue(State.FAILED)
                    signUpState.removeSource(progressLiveData)
                }
            }
        }
    }
}