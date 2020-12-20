package com.tripfellows.authorization.listeners

import android.widget.Button

interface MainRouter {
    fun showTrip(tripId: Int, creatorUid: String)
    fun tripCreated()
    fun signOut()
    fun showShareButton(): Button
    fun hideShareButton()
}