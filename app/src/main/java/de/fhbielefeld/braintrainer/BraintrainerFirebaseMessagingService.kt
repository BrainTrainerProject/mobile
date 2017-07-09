package de.fhbielefeld.braintrainer

import android.util.Log
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage

class BraintrainerFirebaseMessagingService : FirebaseMessagingService() {

    private companion object {
        val TAG = "BtrFirebaseMessaging"
    }

    override fun onMessageReceived(remoteMessage: RemoteMessage?) {
        if(remoteMessage != null) {
            Log.d(TAG, "From: ${remoteMessage.from}")

            // Check if message contains a data payload
            if (remoteMessage.data.size > 0) {
                Log.d(TAG, "Message data payload: ${remoteMessage.data}")

                if (/* Check if data needs to be processed by long running job */ true) {
                    // For long-running tasks (10 seconds or more) use Firebase Job Dispatcher.
                    // TODO: Schedule long running job
                } else {
                    // Handle message within 10 seconds
                    // TODO: Handle now
                }
            }

            // Check if message contains a notification payload
            if(remoteMessage.notification != null) {
                Log.d(TAG, "Message Notification Body: ${remoteMessage.notification.body}")
            }

            // TODO: Build notification if required
        }
    }

}