package com.tripfellows.authorization.repo

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.os.Looper
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.requestPermissions
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.*
import com.google.android.gms.location.LocationServices.getFusedLocationProviderClient
import com.google.maps.model.LatLng
import com.tripfellows.authorization.ApplicationModified
import com.tripfellows.authorization.MainActivity


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
        if (!existsAccessFineLocationPermission() && !existsAccessCoarseLocationPermission()) {
            requestPermissions(MainActivity(),
                arrayOf(ACCESS_FINE_LOCATION), 1)
            requestPermissions(MainActivity(),
                arrayOf(ACCESS_COARSE_LOCATION), 2)
        }
    }

    private fun existsAccessFineLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(context,
            ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun existsAccessCoarseLocationPermission(): Boolean {
        return ActivityCompat.checkSelfPermission(context,
            ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    fun onLocationChanged(location: Location) {
        val coordinates = LatLng(location.latitude, location.longitude)
        userLocation.postValue(coordinates)
    }

    fun getLastLocation(): MutableLiveData<LatLng> {
        return userLocation
    }
}