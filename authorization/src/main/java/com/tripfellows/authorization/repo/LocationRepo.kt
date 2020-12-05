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
import com.tripfellows.authorization.network.response.APIResponse
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

    fun getAddress(latLng: LatLng): LiveData<APIResponse<GeocodingResult>> {
        val apiResponse = APIResponse<GeocodingResult>(RequestProgress.IN_PROGRESS, null, "")
        val geocodingProgress = MutableLiveData(apiResponse)

        GeocodingApi.reverseGeocode(GeoApiContext.Builder().apiKey(API_KEY).build(), latLng)
            .setCallback(object : PendingResult.Callback<Array<GeocodingResult>> {
                override fun onFailure(e: Throwable?) {
                    apiResponse.requestProgress = RequestProgress.FAILED
                    apiResponse.errorMessage = e?.message.toString()
                    geocodingProgress.postValue(apiResponse)
                }

                override fun onResult(result: Array<GeocodingResult>?) {
                    if (result.isNullOrEmpty()) {
                        apiResponse.requestProgress = RequestProgress.FAILED
                        apiResponse.errorMessage = "NOT FOUND"
                    } else {
                        apiResponse.requestProgress = RequestProgress.SUCCESS
                        apiResponse.data = result[0]
                        apiResponse.errorMessage = ""
                        geocodingProgress.postValue(apiResponse)
                    }
                }
            })

        return geocodingProgress
    }
}
