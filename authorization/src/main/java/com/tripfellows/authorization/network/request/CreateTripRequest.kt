package com.tripfellows.authorization.network.request

import java.util.*

class CreateTripRequest(
    var departureAddress: String,
    var destinationAddress: String,
    var placesCount: Int,
    var startDate: String,
    var price: Int,
    var comment: String
)