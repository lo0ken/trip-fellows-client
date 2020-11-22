package com.tripfellows.authorization

import android.app.Application
import android.content.Context
import com.tripfellows.authorization.repo.UserLocationRepo
import com.tripfellows.authorization.network.ApiRepo
import com.tripfellows.authorization.repo.AuthRepo
import com.tripfellows.authorization.repo.LocationRepo
import com.tripfellows.authorization.repo.TripRepo

class ApplicationModified : Application() {

    private lateinit var apiRepo: ApiRepo
    lateinit var authRepo: AuthRepo
    lateinit var tripRepo: TripRepo
    lateinit var locationRepo: LocationRepo
    lateinit var userLocationRepo: UserLocationRepo

    override fun onCreate() {
        super.onCreate()
        apiRepo = ApiRepo()
        authRepo = AuthRepo(apiRepo)
        tripRepo = TripRepo(apiRepo)
        locationRepo = LocationRepo()
        userLocationRepo = UserLocationRepo(applicationContext)
    }

    companion object {
        fun from(context: Context): ApplicationModified {
            return context.applicationContext as ApplicationModified
        }
    }
}