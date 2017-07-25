package de.fhbielefeld.braintrainer.firebase

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService
import de.fhbielefeld.braintrainer.MainActivity
import de.fhbielefeld.braintrainer.network.FirebaseTokenToServer

class BraintrainerFirebaseInstanceIdService : FirebaseInstanceIdService() {

    companion object {
        private val TAG = "BtrFirebaseIId"
    }

    override fun onTokenRefresh() {
        val refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Refreshed token: $refreshedToken")
        sendRegistrationToServer(refreshedToken.toString())
    }

    private fun sendRegistrationToServer(refreshedToken: String) {
        if(MainActivity.idToken != null) {
            FirebaseTokenToServer().execute(MainActivity.idToken, refreshedToken)
        }
    }
}