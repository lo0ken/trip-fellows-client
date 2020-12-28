package com.tripfellows.authorization.services

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import com.tripfellows.authorization.MainActivity
import com.tripfellows.authorization.network.request.UpdateFcmTokenRequest
import com.tripfellows.authorization.repo.FcmTokenRepo

class FirebaseMessageReceivingService : FirebaseMessagingService() {

    private val channelId = "channel_id"

    override fun onNewToken(token: String) {
        FcmTokenRepo.getInstance(applicationContext).updateFcmToken(UpdateFcmTokenRequest(token))
        super.onNewToken(token)
    }

    override fun onMessageReceived(message: RemoteMessage) {
        if (message.data.isNotEmpty()) {
            sendNotification(message.notification?.body!!, message.notification?.title!!, message.data)
        }
    }

    private fun sendNotification(messageBody: String, messageTitle: String, data: Map<String, String>) {
        val pendingIntent = createIntentWithData(data)

        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, channelId)
                .setSmallIcon(R.mipmap.sym_def_app_icon)
                .setContentTitle(messageTitle)
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val notificationChannel = NotificationChannel(
                channelId,
                "Main channel",
                NotificationManager.IMPORTANCE_HIGH
            )

            notificationManager.createNotificationChannel(notificationChannel)
            notificationBuilder.setChannelId(channelId)
        }

        notificationManager.notify(0, notificationBuilder.build())
    }

    private fun createIntentWithData(data: Map<String, String>): PendingIntent? {
        val intent =  Intent(this, MainActivity::class.java)

        val tripId = data["tripId"]
        val creatorUid = data["creatorUid"]
        if (tripId != null) {
            intent.putExtra("tripId", tripId)
        }
        if (creatorUid != null) {
            intent.putExtra("creatorUid", creatorUid)
        }

        intent.putExtra("isPush", true)

        return PendingIntent.getActivity(
            this, 0, intent, PendingIntent.FLAG_ONE_SHOT)
    }
}
