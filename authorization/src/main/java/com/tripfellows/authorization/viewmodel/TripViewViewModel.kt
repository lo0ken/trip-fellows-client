package com.tripfellows.authorization.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tripfellows.authorization.model.Trip
import com.tripfellows.authorization.repo.TripRepo

class TripViewViewModel(application: Application) :  AndroidViewModel(application) {

    private var tripRepo: TripRepo = TripRepo.getInstance(getApplication())
    private var trip: MutableLiveData<Trip> = tripRepo.getTrip()

    fun getTrip(): LiveData<Trip> {
        return trip
    }

    fun refresh(tripId: Int) {
        tripRepo.refreshTrip(tripId)
    }
}