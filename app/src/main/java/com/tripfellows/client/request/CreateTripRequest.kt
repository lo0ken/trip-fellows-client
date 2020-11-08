package com.tripfellows.client.request

import java.util.*

class CreateTripRequest(
    var departureAddress: String,
    var destinationAddress: String,
    var places: Int,
    var startDate: Date,
    var price: Int,
    var comment: String
)