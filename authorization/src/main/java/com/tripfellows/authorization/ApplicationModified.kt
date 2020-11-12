package com.tripfellows.authorization

import android.app.Application
import android.content.Context
import com.tripfellows.authorization.network.ApiRepo
import com.tripfellows.authorization.repo.AuthRepo

class ApplicationModified : Application() {

    lateinit var apiRepo: ApiRepo
    lateinit var authRepo: AuthRepo

    override fun onCreate() {
        super.onCreate()
        apiRepo = ApiRepo()
        authRepo = AuthRepo(apiRepo)
    }

    companion object {
        fun from(context: Context): ApplicationModified {
            return context.applicationContext as ApplicationModified
        }
    }
}