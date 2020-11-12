package com.tripfellows.authorization.listeners

import com.tripfellows.authorization.request.LoginRequest
import com.tripfellows.authorization.request.SignUpRequest

interface Router {
    fun goToSignUp()
    fun mainMenu()
}