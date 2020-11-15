package com.tripfellows.authorization.network

import com.tripfellows.authorization.network.request.CreateAccountRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AccountApi {

    @POST("accounts")
    fun createAccount(@Body account: CreateAccountRequest): Call<CreateAccountRequest>
}