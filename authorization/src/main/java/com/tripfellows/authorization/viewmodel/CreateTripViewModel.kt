package com.tripfellows.authorization.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.tripfellows.authorization.network.request.CreateTripRequest
import com.tripfellows.authorization.repo.TripRepo
import com.tripfellows.authorization.states.CreateTripProgress
import com.tripfellows.authorization.states.CreateTripState

class CreateTripViewModel(application: Application) : AndroidViewModel(application) {

    private val createTripState: MediatorLiveData<CreateTripState> = MediatorLiveData()

    init {
        createTripState.value = CreateTripState.NONE
    }

    fun getProgress(): LiveData<CreateTripState> {
        return createTripState
    }

    fun createTrip(createTripRequest: CreateTripRequest) {
        createTripState.postValue(CreateTripState.IN_PROGRESS)

        val progressLiveData = TripRepo.getInstance(getApplication()).createTrip(createTripRequest)

        if (createTripState.value != CreateTripState.IN_PROGRESS) {
            createTripState.addSource(progressLiveData) {
                if (it == CreateTripProgress.SUCCESS) {
                    createTripState.postValue(CreateTripState.SUCCESS)
                    createTripState.removeSource(progressLiveData)
                } else if (it == CreateTripProgress.FAILED) {
                    createTripState.postValue(CreateTripState.FAILED)
                    createTripState.removeSource(progressLiveData)
                }
            }
        }
    }
}