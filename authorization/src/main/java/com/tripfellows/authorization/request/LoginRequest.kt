package com.tripfellows.authorization.request

data class LoginRequest(
    val email: String,
    val password: String
)