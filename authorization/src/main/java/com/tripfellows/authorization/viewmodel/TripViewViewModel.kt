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
import com.tripfellows.authorization.states.StateWithError

class TripViewViewModel(application: Application) :  AndroidViewModel(application) {

    private var tripRepo: TripRepo = TripRepo.getInstance(getApplication())
    private var trip: MutableLiveData<Trip> = tripRepo.getTrip()
    private val joiningStateWithError: MediatorLiveData<StateWithError> = MediatorLiveData()
    private val removingState: MediatorLiveData<ActionState> = MediatorLiveData()

    init {
        val stateWithError = StateWithError(ActionState.IN_PROGRESS, "")
        joiningStateWithError.postValue(stateWithError)
    }

    fun getTrip(): LiveData<Trip> {
        return trip
    }

    fun refresh(tripId: Int) {
        tripRepo.refreshTrip(tripId)
    }

    fun getJoiningState(): MediatorLiveData<StateWithError> {
        return joiningStateWithError
    }

    fun getRemovingState(): MediatorLiveData<ActionState> {
        return removingState
    }

    fun joinMember(tripId: Int) {
        joiningStateWithError.postValue(StateWithError(ActionState.IN_PROGRESS, ""))
        val progressLiveData = tripRepo.joinMember(JoinMemberRequest(tripId))

        joiningStateWithError.addSource(progressLiveData) {
            if (it.requestProgress == RequestProgress.SUCCESS) {
                joiningStateWithError.postValue(StateWithError(ActionState.SUCCESS, ""))
                joiningStateWithError.removeSource(progressLiveData)
            } else if (it.requestProgress == RequestProgress.FAILED) {
                joiningStateWithError.postValue(StateWithError(ActionState.FAILED, it.errorMessage))
                joiningStateWithError.removeSource(progressLiveData)
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