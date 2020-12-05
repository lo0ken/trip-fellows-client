package com.tripfellows.authorization.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.tripfellows.authorization.network.request.CreateTripRequest
import com.tripfellows.authorization.repo.TripRepo
import com.tripfellows.authorization.states.RequestProgress
import com.tripfellows.authorization.states.ActionStatus

class CreateTripViewModel(application: Application) : AndroidViewModel(application) {

    private val createTripStatus: MediatorLiveData<ActionStatus> = MediatorLiveData()

    init {
        createTripStatus.value = ActionStatus.NONE
    }

    fun getProgress(): LiveData<ActionStatus> {
        return createTripStatus
    }

    fun createTrip(createTripRequest: CreateTripRequest) {
        createTripStatus.postValue(ActionStatus.IN_PROGRESS)

        val progressLiveData = TripRepo.getInstance(getApplication()).createTrip(createTripRequest)

        createTripStatus.addSource(progressLiveData) {
            if (it == RequestProgress.SUCCESS) {
                createTripStatus.postValue(ActionStatus.SUCCESS)
                createTripStatus.removeSource(progressLiveData)
            } else if (it == RequestProgress.FAILED) {
                createTripStatus.postValue(ActionStatus.FAILED)
                createTripStatus.removeSource(progressLiveData)
            }
        }
    }
}