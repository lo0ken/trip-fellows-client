package com.tripfellows.authorization.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.tripfellows.authorization.network.request.CreateTripRequest
import com.tripfellows.authorization.repo.TripRepo
import com.tripfellows.authorization.states.RequestProgress
import com.tripfellows.authorization.states.ActionState

class CreateTripViewModel(application: Application) : AndroidViewModel(application) {

    private val createTripState: MediatorLiveData<ActionState> = MediatorLiveData()

    init {
        createTripState.value = ActionState.NONE
    }

    fun getProgress(): LiveData<ActionState> {
        return createTripState
    }

    fun createTrip(createTripRequest: CreateTripRequest) {
        createTripState.postValue(ActionState.IN_PROGRESS)

        val progressLiveData = TripRepo.getInstance(getApplication()).createTrip(createTripRequest)

        createTripState.addSource(progressLiveData) {
            if (it == RequestProgress.SUCCESS) {
                createTripState.postValue(ActionState.SUCCESS)
                createTripState.removeSource(progressLiveData)
            } else if (it == RequestProgress.FAILED) {
                createTripState.postValue(ActionState.FAILED)
                createTripState.removeSource(progressLiveData)
            }
        }
    }
}