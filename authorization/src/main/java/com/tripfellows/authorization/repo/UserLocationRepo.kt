package com.tripfellows.authorization.repo

import android.content.Context
import android.location.Location
import android.os.Looper
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.maps.model.LatLng
import com.tripfellows.authorization.ApplicationModified


class UserLocationRepo(private var context: Context) {
    private val userLocation: MutableLiveData<LatLng> = MutableLiveData()
    private val LOCATION_REQUEST_INTERVAL: Long = 10000
    private val LOCATION_REQUEST_FASTEST_INTERVAL: Long = 3000

    companion object {
        fun getInstance(context: Context): UserLocationRepo {
            return ApplicationModified.from(context).userLocationRepo
        }
    }

    private var locationRequest = LocationRequest()

    fun startLocationUpdates() {
        locationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        locationRequest.interval = LOCATION_REQUEST_INTERVAL
        locationRequest.fastestInterval = LOCATION_REQUEST_FASTEST_INTERVAL

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(context)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        checkPermission()

        getFusedLocationProviderClient(context).requestLocationUpdates(locationRequest,
            object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    onLocationChanged(locationResult.lastLocation)
                }
            },
            Looper.myLooper())
    }

    private fun checkPermission() {

    }

    fun onLocationChanged(location: Location) {
        val coordinates = LatLng(location.latitude, location.longitude)
        userLocation.postValue(coordinates)
    }

    fun getLastLocation(): MutableLiveData<LatLng> {
        return userLocation
    }
}