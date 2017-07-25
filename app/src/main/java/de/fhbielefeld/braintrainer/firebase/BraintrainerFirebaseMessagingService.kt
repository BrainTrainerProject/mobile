package de.fhbielefeld.braintrainer.firebase

import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Intent
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import de.fhbielefeld.braintrainer.MainActivity
import de.fhbielefeld.braintrainer.R

class BraintrainerFirebaseMessagingService : FirebaseMessagingService() {

    companion object {
        private val TAG = "BtrFirebaseMessaging"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        if(remoteMessage != null) {
            if (remoteMessage.data.isNotEmpty()) {
                val map = remoteMessage.data
                if(map.containsKey("ueben")){
                    if(map.get("ueben") == "true") {
                        buildNotification(remoteMessage.notification.body)
                    }
                }
            }
        }
    }

    private fun buildNotification(messageBody: String?) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("ueben", "true")
        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT)

        val notificationBuilder = Notification.Builder(this)
                .setSmallIcon(R.mipmap.braintrainer_mobile_notifiaction_icon)
                .setContentTitle("Es ist Zeit zu üben")
                .setContentText(messageBody)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)

        val notificationManager =
                this.getSystemService(android.content.Context.NOTIFICATION_SERVICE) as NotificationManager

        notificationManager.notify(0, notificationBuilder.build())
    }

}