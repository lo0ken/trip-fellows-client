package com.tripfellows.authorization.model

data class Trip(
    val id: Int,
    val departureAddress: Address,
    val destinationAddress: Address,
    val startDate: String,
    val placesCount: Int,
    val price: String
)