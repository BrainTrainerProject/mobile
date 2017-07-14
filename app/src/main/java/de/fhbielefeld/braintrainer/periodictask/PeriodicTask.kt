package de.fhbielefeld.braintrainer.periodictask

import android.os.Handler
import android.util.Log
import android.webkit.ValueCallback
import android.webkit.WebView
import com.google.firebase.iid.FirebaseInstanceId
import de.fhbielefeld.braintrainer.MainActivity

class PeriodicTask(webview: WebView) : Runnable {

    companion object {
        private val TAG: String = "PeriodicTask"
        private val interval: Long = 5000
        private val idTokenName: String = "auh0-driver-tokens"
    }

    private val webView: WebView = webview
    private val handler: Handler? = Handler()

    private var queryJS: Boolean = true

    override fun run() {
        try {
            webView.evaluateJavascript("(function() {return localStorage.getItem('${idTokenName}').toString(); })();",
                    object:ValueCallback<String> {
                        override fun onReceiveValue(value: String?) {
                            Log.d(TAG, "Value returned from JavaScript: $value")
                            if(value != null && value != "null") {
                                val idToken: String = value.substring(value.indexOf("idToken") + 12, value.lastIndexOf("\"") - 3)
                                queryJS = false
                                MainActivity.idToken = idToken
                                MainActivity.runAsyncTask(idToken, FirebaseInstanceId.getInstance().token)
                            }
                        }
                    })
        } finally {
            if(queryJS) {
                handler?.postDelayed(this, interval)
            }
        }
    }
}