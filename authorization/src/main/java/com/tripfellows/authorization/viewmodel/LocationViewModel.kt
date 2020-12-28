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
import com.tripfellows.authorization.states.ActionStatus
import com.tripfellows.authorization.states.RequestProgress

class LocationViewModel(application: Application) : AndroidViewModel(application) {

    private val locationStatus: MediatorLiveData<ActionStatus> = MediatorLiveData()
    private val currentAddress: MediatorLiveData<Address> = MediatorLiveData()
    private var userLocationRepo: UserLocationRepo
    private val lastLocation: MediatorLiveData<ActionStatus> = MediatorLiveData()

    init {
        locationStatus.value = ActionStatus.NONE
        currentAddress.value = null
        lastLocation.value = ActionStatus.NONE
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
        locationStatus.postValue(ActionStatus.IN_PROGRESS)

        if (currentAddress.value != null) {
            return
        }

        val progressLiveData = LocationRepo.getInstance(getApplication())
            .getAddress(userLocation)

        if (locationStatus.value != ActionStatus.IN_PROGRESS) {
            currentAddress.addSource(progressLiveData) {
                if (it.requestProgress == RequestProgress.SUCCESS) {
                    locationStatus.postValue(ActionStatus.SUCCESS)
                    locationStatus.removeSource(progressLiveData)

                    currentAddress.postValue(buildAddress(progressLiveData.value?.data))
                    currentAddress.removeSource(progressLiveData)
                    userLocationRepo.stopLocationUpdates()
                } else if (it.requestProgress == RequestProgress.FAILED) {
                    locationStatus.postValue(ActionStatus.FAILED)
                    locationStatus.removeSource(progressLiveData)

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