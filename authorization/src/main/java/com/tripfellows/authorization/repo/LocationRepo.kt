package com.tripfellows.authorization.repo

import android.content.Context
import android.content.pm.PackageManager
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.maps.GeoApiContext
import com.google.maps.GeocodingApi
import com.google.maps.PendingResult
import com.google.maps.model.GeocodingResult
import com.google.maps.model.LatLng
import com.tripfellows.authorization.ApplicationModified
import com.tripfellows.authorization.network.response.ResultResponse
import com.tripfellows.authorization.states.RequestProgress


class LocationRepo {

    companion object {
        private lateinit var API_KEY : String

        fun getInstance(context: Context): LocationRepo {
            this.API_KEY = context.packageManager.getApplicationInfo(context.packageName, PackageManager.GET_META_DATA)
                .metaData.get("com.google.android.maps.v2.API_KEY").toString()
            return ApplicationModified.from(context).locationRepo
        }
    }

    fun getAddress(latLng: LatLng): LiveData<ResultResponse<GeocodingResult>> {
        val resultResponse =
            ResultResponse<GeocodingResult>(
                RequestProgress.IN_PROGRESS,
                null,
                null
            )
        val geocodingProgress = MutableLiveData(resultResponse)

        GeocodingApi.reverseGeocode(GeoApiContext.Builder().apiKey(API_KEY).build(), latLng)
            .setCallback(object : PendingResult.Callback<Array<GeocodingResult>> {
                override fun onFailure(e: Throwable?) {
                    resultResponse.requestProgress = RequestProgress.FAILED
                    resultResponse.errorMessage = e?.message
                    geocodingProgress.postValue(resultResponse)
                }

                override fun onResult(result: Array<GeocodingResult>?) {
                    if (result.isNullOrEmpty()) {
                        resultResponse.requestProgress = RequestProgress.FAILED
                        resultResponse.errorMessage = "NOT FOUND"
                    } else {
                        resultResponse.requestProgress = RequestProgress.SUCCESS
                        resultResponse.data = result[0]
                        geocodingProgress.postValue(resultResponse)
                    }
                }
            })

        return geocodingProgress
    }
}
