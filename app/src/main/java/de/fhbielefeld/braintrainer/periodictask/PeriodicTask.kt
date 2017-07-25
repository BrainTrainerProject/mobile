package de.fhbielefeld.braintrainer.periodictask

import android.os.Handler
import android.webkit.WebView
import com.google.firebase.iid.FirebaseInstanceId
import de.fhbielefeld.braintrainer.MainActivity
import de.fhbielefeld.braintrainer.network.FirebaseTokenToServer

class PeriodicTask(private val webView: WebView) : Runnable {

    companion object {
        private val TAG: String = "PeriodicTask"
        private val interval: Long = 5000
        private val idTokenName: String = "auh0-driver-tokens"
    }

    private val handler: Handler = Handler()

    private var repeat: Boolean = true

    override fun run() {
        webView.evaluateJavascript("(function() {return localStorage.getItem('$idTokenName').toString(); })();"
        ) { value ->
            if(value != null && value != "null") {
                val idToken: String = value.substring(value.indexOf("idToken") + 12, value.lastIndexOf("\"") - 3)
                repeat = false
                MainActivity.idToken = idToken
                FirebaseTokenToServer().execute(idToken, FirebaseInstanceId.getInstance().token)
            }
        }

        if(repeat) {
            handler.postDelayed(this, interval)
        }
    }
}