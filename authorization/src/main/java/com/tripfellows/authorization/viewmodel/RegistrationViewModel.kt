package com.tripfellows.authorization.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.tripfellows.authorization.repo.AuthRepo
import com.tripfellows.authorization.request.SignUpRequest
import com.tripfellows.authorization.states.SignUpProgress
import com.tripfellows.authorization.states.SignUpState

class RegistrationViewModel(application: Application) : AndroidViewModel(application) {
    private val signUpState: MediatorLiveData<SignUpState> = MediatorLiveData()

    init {
        signUpState.value = SignUpState.NONE
    }

    fun getProgress(): LiveData<SignUpState> {
        return signUpState
    }

    fun signUp(signUpRequest: SignUpRequest) {
        signUpState.postValue(SignUpState.IN_PROGRESS)

        val progressLiveData = AuthRepo.getInstance(getApplication()).signUp(signUpRequest)

        if (signUpState.value != SignUpState.IN_PROGRESS) {
            signUpState.addSource(progressLiveData) {
                if (it == SignUpProgress.SUCCESS) {
                    signUpState.postValue(SignUpState.SUCCESS)
                    signUpState.removeSource(progressLiveData)
                } else if (it == SignUpProgress.FAILED) {
                    signUpState.postValue(SignUpState.FAILED)
                    signUpState.removeSource(progressLiveData)
                }
            }
        }
    }
}