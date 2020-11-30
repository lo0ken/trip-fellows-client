package com.tripfellows.authorization.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.tripfellows.authorization.model.Trip
import com.tripfellows.authorization.repo.TripRepo

class TripListViewModel(application: Application) :  AndroidViewModel(application) {

    private var tripRepo: TripRepo = TripRepo.getInstance(getApplication())
    private var trips: LiveData<List<Trip>> = tripRepo.getTrips()


    fun getTrips(): LiveData<List<Trip>> {
        return trips;
    }

    fun refresh() {
        tripRepo.refreshTrips()
    }

}