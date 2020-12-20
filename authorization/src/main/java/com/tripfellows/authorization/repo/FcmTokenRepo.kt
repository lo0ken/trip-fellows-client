package com.tripfellows.authorization.repo

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tripfellows.authorization.ApplicationModified
import com.tripfellows.authorization.network.ApiRepo
import com.tripfellows.authorization.network.request.UpdateFcmTokenRequest
import com.tripfellows.authorization.states.RequestProgress
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FcmTokenRepo(private val apiRepo: ApiRepo) {

    companion object {
        fun getInstance(context: Context): FcmTokenRepo {
            return ApplicationModified.from(context).fcmTokenRepo
        }
    }

    fun updateFcmToken(updateFcmTokenRequest: UpdateFcmTokenRequest): LiveData<RequestProgress> {
        val updateProgress: MutableLiveData<RequestProgress> = MutableLiveData()
        updateProgress.value = RequestProgress.IN_PROGRESS

        apiRepo.fcmTokenApi.updateFcmToken(updateFcmTokenRequest).enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    updateProgress.postValue(RequestProgress.SUCCESS)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                updateProgress.postValue(RequestProgress.FAILED)
            }
        })
        return updateProgress
    }

    fun deleteFcmToken(): LiveData<RequestProgress> {
        val deleteProgress: MutableLiveData<RequestProgress> = MutableLiveData()
        deleteProgress.value = RequestProgress.IN_PROGRESS

        apiRepo.fcmTokenApi.deleteFcmToken().enqueue(object: Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    deleteProgress.postValue(RequestProgress.SUCCESS)
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                deleteProgress.postValue(RequestProgress.FAILED)
            }
        })
        return deleteProgress
    }
}