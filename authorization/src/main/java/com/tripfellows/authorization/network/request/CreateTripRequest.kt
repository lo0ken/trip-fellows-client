package com.tripfellows.authorization.network.request

import com.tripfellows.authorization.fragment.CreateTripFragment
import java.util.*

class CreateTripRequest(
    var startPoint: CreateTripFragment.Point,
    var endPoint: CreateTripFragment.Point,
    var placesCount: Int,
    var startDate: String,
    var price: Int,
    var comment: String
)