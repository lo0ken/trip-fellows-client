package com.tripfellows.authorization.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.google.maps.model.GeocodingResult
import com.google.maps.model.LatLng
import com.tripfellows.authorization.model.Address
import com.tripfellows.authorization.repo.LocationRepo
import com.tripfellows.authorization.repo.UserLocationRepo
import com.tripfellows.authorization.states.Progress
import com.tripfellows.authorization.states.State

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val locationState: MediatorLiveData<State> = MediatorLiveData()
    private val currentAddress: MediatorLiveData<Address> = MediatorLiveData()
    private var userLocationRepo: UserLocationRepo
    private val lastLocation: MediatorLiveData<State> = MediatorLiveData()

    init {
        locationState.value = State.NONE
        currentAddress.value = null
        lastLocation.value = State.NONE
        userLocationRepo = UserLocationRepo(application.baseContext)
        userLocationRepo.startLocationUpdates()
    }

    fun getAddress(): LiveData<Address> {
        return currentAddress
    }

    fun getCurrentAddress() {
        val userLocationLiveData = userLocationRepo.getLastLocation()

        userLocationLiveData.observeForever {
            getFromUserLocation(it)
        }
    }

    private fun getFromUserLocation(userLocation: LatLng) {
        locationState.postValue(State.IN_PROGRESS)

        val progressLiveData = LocationRepo.getInstance(getApplication())
            .getAddress(userLocation)

        if (locationState.value != State.IN_PROGRESS) {
            currentAddress.addSource(progressLiveData) {
                if (it.progress == Progress.SUCCESS) {
                    locationState.postValue(State.SUCCESS)
                    locationState.removeSource(progressLiveData)
                    currentAddress.postValue(buildAddress(progressLiveData.value?.data))
                    currentAddress.removeSource(progressLiveData)
                } else if (it.progress == Progress.FAILED) {
                    locationState.postValue(State.FAILED)
                    locationState.removeSource(progressLiveData)

                    currentAddress.postValue(null)
                    currentAddress.removeSource(progressLiveData)
                }
            }
        }
    }

    private fun buildAddress(addressData: GeocodingResult?): Address? {
        val address = Address()
        address.id = addressData?.placeId.toString()
        address.name = addressData?.formattedAddress.toString()
        address.latitude = addressData?.geometry?.location?.lat
        address.longitude = addressData?.geometry?.location?.lng

        return address
    }
}