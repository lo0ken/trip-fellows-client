package com.tripfellows.authorization.util

object ValidationPatterns {
    const val SEATS_NUMBER = "^[1-5]$"
    const val PASSWORD = ".{6,}"
    const val PHONE_NUMBER = "^((\\+7|7|8)+([0-9]){10})\$"
}