package com.tripfellows.authorization.network.response

data class APIError (
     val statusCode : Int,
     val message: String?
)