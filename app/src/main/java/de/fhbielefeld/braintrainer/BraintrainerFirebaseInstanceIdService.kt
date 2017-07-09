package de.fhbielefeld.braintrainer

import android.util.Log
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.iid.FirebaseInstanceIdService

class BraintrainerFirebaseInstanceIdService : FirebaseInstanceIdService() {

    private companion object {
        val TAG = "BtrFirebaseIId"
    }

    override fun onTokenRefresh() {
        var refreshedToken = FirebaseInstanceId.getInstance().token
        Log.d(TAG, "Refreshed token: $refreshedToken")
        sendRegistrationToServer(refreshedToken.toString())
    }

    private fun sendRegistrationToServer(refreshedToken: String) {
        // TODO: Custom implementation
    }
}