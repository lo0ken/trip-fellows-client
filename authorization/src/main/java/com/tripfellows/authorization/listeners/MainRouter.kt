package com.tripfellows.authorization.listeners

interface MainRouter {
    fun showTrip(tripId: Int, creatorUid: String)
}