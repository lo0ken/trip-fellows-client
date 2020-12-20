package com.tripfellows.authorization.services

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tripfellows.authorization.MainActivity
import com.tripfellows.authorization.network.request.UpdateFcmTokenRequest
import com.tripfellows.authorization.repo.FcmTokenRepo


class FirebaseMessageReceivingService : FirebaseMessagingService() {

    override fun onNewToken(p0: String) {
        FcmTokenRepo.getInstance(applicationContext).updateFcmToken(UpdateFcmTokenRequest(p0))
        super.onNewToken(p0)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        if (message.data.isNotEmpty()) {

        }
    }

    private fun sendNotification(
        messageBody: String,
        messageTitle: String,
        data: HashMap<String, String>
    ) {

    }
}
