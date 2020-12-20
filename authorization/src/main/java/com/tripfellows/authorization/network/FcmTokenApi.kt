package com.tripfellows.authorization.network

import com.tripfellows.authorization.network.request.UpdateFcmTokenRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

interface FcmTokenApi {

    @POST("fcm/updateFcmToken")
    fun updateFcmToken(@Body updateFcmTokenRequest: UpdateFcmTokenRequest): Call<Void>

    @DELETE("fcm/deleteFcmToken")
    fun deleteFcmToken(): Call<Void>
}