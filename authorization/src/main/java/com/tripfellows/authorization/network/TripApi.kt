package com.tripfellows.authorization.network

import com.tripfellows.authorization.model.Trip
import com.tripfellows.authorization.network.request.CreateTripRequest
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface TripApi {

    @POST("trips")
    fun createTrip(@Body createTripRequest: CreateTripRequest): Call<CreateTripRequest>

    @GET("trips")
    fun getAllTrips(): Call<List<Trip>>

    @GET("trips/{id}")
    fun getTrip(@Path("id") tripId: Int): Call<Trip>
}