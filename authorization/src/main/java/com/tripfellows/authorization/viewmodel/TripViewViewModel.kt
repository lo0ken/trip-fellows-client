package com.tripfellows.authorization.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.tripfellows.authorization.model.Trip
import com.tripfellows.authorization.model.TripStatusCodeEnum
import com.tripfellows.authorization.network.request.JoinMemberRequest
import com.tripfellows.authorization.repo.TripRepo
import com.tripfellows.authorization.states.ActionState
import com.tripfellows.authorization.states.RequestProgress

class TripViewViewModel(application: Application) :  AndroidViewModel(application) {

    private var tripRepo: TripRepo = TripRepo.getInstance(getApplication())
    private var trip: MutableLiveData<Trip> = tripRepo.getTrip()
    private val joiningState: MediatorLiveData<ActionState> = MediatorLiveData()
    private val removingState: MediatorLiveData<ActionState> = MediatorLiveData()


    fun getTrip(): LiveData<Trip> {
        return trip
    }

    fun refresh(tripId: Int) {
        tripRepo.refreshTrip(tripId)
    }

    fun getJoiningState(): MediatorLiveData<ActionState> {
        return joiningState;
    }

    fun getRemovingState(): MediatorLiveData<ActionState> {
        return removingState
    }

    fun joinMember(tripId: Int) {
        joiningState.postValue(ActionState.IN_PROGRESS)

        val progressLiveData = tripRepo.joinMember(JoinMemberRequest(tripId))

        joiningState.addSource(progressLiveData) {
            if (it == RequestProgress.SUCCESS) {
                joiningState.postValue(ActionState.SUCCESS)
                joiningState.removeSource(progressLiveData)
            } else if (it == RequestProgress.FAILED) {
                joiningState.postValue(ActionState.FAILED)
                joiningState.removeSource(progressLiveData)
            }
        }

        refresh(tripId)
    }

    fun removeMember(tripMemberId: Int) {
        removingState.postValue(ActionState.IN_PROGRESS)
        val progressLiveData = tripRepo.removeMember(tripMemberId)


        removingState.addSource(progressLiveData) {
            if (it == RequestProgress.SUCCESS) {
                removingState.postValue(ActionState.SUCCESS)
                removingState.removeSource(progressLiveData)
            } else if (it == RequestProgress.FAILED) {
                removingState.postValue(ActionState.FAILED)
                removingState.removeSource(progressLiveData)
            }

        }

        refresh(trip.value!!.id)
    }

    fun changeStatus(tripId: Int, status: TripStatusCodeEnum) {
        tripRepo.changeStatus(tripId, status)
    }
}