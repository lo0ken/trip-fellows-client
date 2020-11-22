package com.tripfellows.authorization.repo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.maps.GeocodingApi
import com.google.maps.PendingResult
import com.google.maps.model.GeocodingResult
import com.google.maps.model.LatLng
import com.tripfellows.authorization.ApplicationModified
import com.tripfellows.authorization.network.request.ResultResponse
import com.tripfellows.authorization.states.Progress
import com.tripfellows.authorization.util.LocationUtil.getGeoApiContext


class LocationRepo {

    companion object {
        fun getInstance(context: Context): LocationRepo {
            return ApplicationModified.from(context).locationRepo
        }
    }

    fun getAddress(latLng: LatLng): LiveData<ResultResponse<GeocodingResult>> {
        val dataResult = ResultResponse<GeocodingResult>(Progress.IN_PROGRESS, null, null)
        val geocodingProgress = MutableLiveData(dataResult)

        GeocodingApi.reverseGeocode(getGeoApiContext(), latLng)
            .setCallback(object : PendingResult.Callback<Array<GeocodingResult>> {
                override fun onFailure(e: Throwable?) {
                    dataResult.progress = Progress.FAILED
                    dataResult.errorMessage = e?.message
                    geocodingProgress.postValue(dataResult)
                }

                override fun onResult(result: Array<GeocodingResult>?) {
                    if (result.isNullOrEmpty()) {
                        dataResult.progress = Progress.FAILED
                        dataResult.errorMessage = "NOT FOUND"
                    } else {
                        dataResult.progress = Progress.SUCCESS
                        dataResult.data = result[0]
                        geocodingProgress.postValue(dataResult)
                    }
                }
            })

        return geocodingProgress
    }
}
