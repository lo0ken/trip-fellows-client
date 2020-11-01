package com.tripfellows.authorization.request

data class LoginRequest(
    val username: String,
    val password: String
)