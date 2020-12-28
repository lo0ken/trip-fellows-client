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
import com.tripfellows.authorization.states.ActionStatus
import com.tripfellows.authorization.states.RequestProgress
import com.tripfellows.authorization.states.ActionState

class TripViewViewModel(application: Application) :  AndroidViewModel(application) {

    private var tripRepo: TripRepo = TripRepo.getInstance(getApplication())
    private var trip: MutableLiveData<Trip> = tripRepo.getTrip()
    private val joiningActionState: MediatorLiveData<ActionState> = MediatorLiveData()
    private val removingStatus: MediatorLiveData<ActionStatus> = MediatorLiveData()

    init {
        val stateWithError = ActionState(ActionStatus.IN_PROGRESS, "")
        joiningActionState.postValue(stateWithError)
    }

    fun getTrip(): LiveData<Trip> {
        return trip
    }

    fun refresh(tripId: Int) {
        tripRepo.refreshTrip(tripId)
    }

    fun getJoiningState(): MediatorLiveData<ActionState> {
        return joiningActionState
    }

    fun getRemovingState(): MediatorLiveData<ActionStatus> {
        return removingStatus
    }

    fun joinMember(tripId: Int) {
        joiningActionState.postValue(ActionState(ActionStatus.IN_PROGRESS, ""))
        val progressLiveData = tripRepo.joinMember(JoinMemberRequest(tripId))

        joiningActionState.addSource(progressLiveData) {
            if (it.requestProgress == RequestProgress.SUCCESS) {
                joiningActionState.postValue(ActionState(ActionStatus.SUCCESS, ""))
                joiningActionState.removeSource(progressLiveData)
            } else if (it.requestProgress == RequestProgress.FAILED) {
                joiningActionState.postValue(ActionState(ActionStatus.FAILED, it.errorMessage))
                joiningActionState.removeSource(progressLiveData)
            }
        }

        refresh(tripId)
    }

    fun removeMember(tripMemberId: Int) {
        removingStatus.postValue(ActionStatus.IN_PROGRESS)
        val progressLiveData = tripRepo.removeMember(tripMemberId)


        removingStatus.addSource(progressLiveData) {
            if (it == RequestProgress.SUCCESS) {
                removingStatus.postValue(ActionStatus.SUCCESS)
                removingStatus.removeSource(progressLiveData)
            } else if (it == RequestProgress.FAILED) {
                removingStatus.postValue(ActionStatus.FAILED)
                removingStatus.removeSource(progressLiveData)
            }

        }

        refresh(trip.value!!.id)
    }

    fun changeStatus(tripId: Int, status: TripStatusCodeEnum) {
        tripRepo.changeStatus(tripId, status)
    }
}