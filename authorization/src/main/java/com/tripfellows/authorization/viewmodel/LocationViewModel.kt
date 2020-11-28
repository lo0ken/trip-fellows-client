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
import com.tripfellows.authorization.states.RequestProgress
import com.tripfellows.authorization.states.ActionState

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val locationState: MediatorLiveData<ActionState> = MediatorLiveData()
    private val currentAddress: MediatorLiveData<Address> = MediatorLiveData()
    private var userLocationRepo: UserLocationRepo
    private val lastLocation: MediatorLiveData<ActionState> = MediatorLiveData()

    init {
        locationState.value = ActionState.NONE
        currentAddress.value = null
        lastLocation.value = ActionState.NONE
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
        locationState.postValue(ActionState.IN_PROGRESS)

        val progressLiveData = LocationRepo.getInstance(getApplication())
            .getAddress(userLocation)

        if (locationState.value != ActionState.IN_PROGRESS) {
            currentAddress.addSource(progressLiveData) {
                if (it.requestProgress == RequestProgress.SUCCESS) {
                    locationState.postValue(ActionState.SUCCESS)
                    locationState.removeSource(progressLiveData)
                    currentAddress.postValue(buildAddress(progressLiveData.value?.data))
                    currentAddress.removeSource(progressLiveData)
                } else if (it.requestProgress == RequestProgress.FAILED) {
                    locationState.postValue(ActionState.FAILED)
                    locationState.removeSource(progressLiveData)

                    currentAddress.postValue(null)
                    currentAddress.removeSource(progressLiveData)
                }
            }
        }
    }

    private fun buildAddress(addressData: GeocodingResult?): Address? {
        val address = Address()
        address.addressId = addressData?.placeId.toString()
        address.address = addressData?.formattedAddress.toString()
        address.latitude = addressData?.geometry?.location?.lat
        address.longitude = addressData?.geometry?.location?.lng

        return address
    }
}