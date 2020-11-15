package com.tripfellows.authorization

import android.app.Application
import android.content.Context
import com.tripfellows.authorization.network.ApiRepo
import com.tripfellows.authorization.repo.AuthRepo
import com.tripfellows.authorization.repo.TripRepo

class ApplicationModified : Application() {

    private lateinit var apiRepo: ApiRepo
    lateinit var authRepo: AuthRepo
    lateinit var tripRepo: TripRepo

    override fun onCreate() {
        super.onCreate()
        apiRepo = ApiRepo()
        authRepo = AuthRepo(apiRepo)
        tripRepo = TripRepo(apiRepo)
    }

    companion object {
        fun from(context: Context): ApplicationModified {
            return context.applicationContext as ApplicationModified
        }
    }
}