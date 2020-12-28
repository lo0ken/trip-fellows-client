package com.tripfellows.authorization

import android.app.Application
import android.content.Context
import com.tripfellows.authorization.network.ApiRepo
import com.tripfellows.authorization.repo.*

class ApplicationModified : Application() {

    private lateinit var apiRepo: ApiRepo
    lateinit var authRepo: AuthRepo
    lateinit var tripRepo: TripRepo
    lateinit var locationRepo: LocationRepo
    lateinit var userLocationRepo: UserLocationRepo
    lateinit var fcmTokenRepo: FcmTokenRepo

    override fun onCreate() {
        super.onCreate()
        apiRepo = ApiRepo()
        authRepo = AuthRepo(apiRepo)
        tripRepo = TripRepo(apiRepo)
        locationRepo = LocationRepo()
        userLocationRepo = UserLocationRepo(applicationContext)
        fcmTokenRepo = FcmTokenRepo(apiRepo)
    }

    companion object {
        fun from(context: Context): ApplicationModified {
            return context.applicationContext as ApplicationModified
        }
    }
}