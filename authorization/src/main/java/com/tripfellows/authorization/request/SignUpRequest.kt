package com.tripfellows.authorization.request

data class SignUpRequest(
    val username: String,
    val password: String,
    val name: String,
    val phoneNumber: String
)