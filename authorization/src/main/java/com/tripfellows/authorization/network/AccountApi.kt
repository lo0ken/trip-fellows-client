package com.tripfellows.authorization.network

import com.tripfellows.authorization.model.Account
import com.tripfellows.authorization.network.request.CreateAccountRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AccountApi {

    @POST("accounts")
    fun createAccount(@Body account: CreateAccountRequest): Call<CreateAccountRequest>

    @GET("accounts/me")
    fun getCurrentAccount(): Call<Account>
}