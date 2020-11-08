package com.tripfellows.authorization.listeners

import com.tripfellows.authorization.request.LoginRequest
import com.tripfellows.authorization.request.SignUpRequest

interface AuthorizationListener {
    fun goToSignUp()
    fun signIn(loginRequest: LoginRequest)
    fun signUp(signUpRequest: SignUpRequest)
}