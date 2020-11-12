package com.tripfellows.authorization.network

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface AccountApi {

    @POST("accounts")
    fun createAccount(@Body account: Account): Call<Account>
}