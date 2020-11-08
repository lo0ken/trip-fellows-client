package com.tripfellows.authorization.request

data class SignUpRequest(
    val email: String,
    val password: String,
    val name: String,
    val phoneNumber: String
)