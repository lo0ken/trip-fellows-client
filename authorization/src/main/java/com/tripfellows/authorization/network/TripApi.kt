package com.tripfellows.authorization.network

import com.tripfellows.authorization.model.Trip
import com.tripfellows.authorization.model.TripMember
import com.tripfellows.authorization.model.TripStatusCodeEnum
import com.tripfellows.authorization.network.request.CreateTripRequest
import com.tripfellows.authorization.network.request.JoinMemberRequest
import retrofit2.Call
import retrofit2.http.*

interface TripApi {

    @POST("trips")
    fun createTrip(@Body createTripRequest: CreateTripRequest): Call<CreateTripRequest>

    @GET("trips")
    fun getAllTrips(): Call<List<Trip>>

    @GET("trips/{id}")
    fun getTrip(@Path("id") tripId: Int): Call<Trip>

    @POST("trip-members/addMember")
    fun joinMember(@Body joinMemberRequest: JoinMemberRequest): Call<TripMember>

    @DELETE("trip-members/removeMember/{tripMemberId}")
    fun removeMember(@Path("tripMemberId") tripMemberId: Int): Call<Void>

    @PUT("trip-status")
    fun changeStatus(@Query("tripId") tripId: Int, @Query("status") status: TripStatusCodeEnum): Call<Trip>
}