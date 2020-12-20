package com.tripfellows.authorization.listeners

interface ConnectionRouter {
    fun reload()
    fun hasInternetConnection() : Boolean
}
