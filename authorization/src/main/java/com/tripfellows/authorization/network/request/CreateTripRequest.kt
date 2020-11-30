package com.tripfellows.authorization.network.request

import com.tripfellows.authorization.model.Address

class CreateTripRequest(
    var departureAddress: Address,
    var destinationAddress: Address,
    var placesCount: Int,
    var startDate: String,
    var price: Int,
    var comment: String
)