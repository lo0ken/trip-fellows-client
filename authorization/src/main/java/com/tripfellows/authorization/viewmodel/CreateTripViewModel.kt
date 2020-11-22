package com.tripfellows.authorization.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.tripfellows.authorization.network.request.CreateTripRequest
import com.tripfellows.authorization.repo.TripRepo
import com.tripfellows.authorization.states.Progress
import com.tripfellows.authorization.states.State

class CreateTripViewModel(application: Application) : AndroidViewModel(application) {

    private val createTripState: MediatorLiveData<State> = MediatorLiveData()

    init {
        createTripState.value = State.NONE
    }

    fun getProgress(): LiveData<State> {
        return createTripState
    }

    fun createTrip(createTripRequest: CreateTripRequest) {
        createTripState.postValue(State.IN_PROGRESS)

        val progressLiveData = TripRepo.getInstance(getApplication()).createTrip(createTripRequest)

        createTripState.addSource(progressLiveData) {
            if (it == Progress.SUCCESS) {
                createTripState.postValue(State.SUCCESS)
                createTripState.removeSource(progressLiveData)
            } else if (it == Progress.FAILED) {
                createTripState.postValue(State.FAILED)
                createTripState.removeSource(progressLiveData)
            }
        }
    }
}