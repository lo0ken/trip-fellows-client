package com.tripfellows.authorization.network.request

data class CreateAccountRequest(
    val name: String,
    val phoneNumber: String
)