package de.fhbielefeld.braintrainer.periodictask

import android.os.Build
import android.os.Handler
import android.util.Log
import android.webkit.ValueCallback
import android.webkit.WebView
import de.fhbielefeld.braintrainer.MainActivity

class PeriodicTask(webview: WebView) : Runnable {

    companion object {
        private val TAG = "PeriodicTask"
        private val interval: Long = 5000
    }

    private val webView: WebView = webview
    private val handler: Handler? = Handler()

    override fun run() {
        try {
            if(Build.VERSION.SDK_INT >= 19) {
                webView.evaluateJavascript("(function() {return localStorage.getItem('${MainActivity.idToken}').toString(); })();",
                        object:ValueCallback<String> {
                            override fun onReceiveValue(value: String?) {
                                Log.d(TAG, "Value returned from JavaScript: $value")
                            }
                        })
            } else {
                webView.loadUrl("javascript: window.AndroidApp.receiveString(" +
                        "function() {" +
                            "var AndroidToken = localStorage.getItem('${MainActivity.idToken}');" +
                            "var resultString = 'undefined';" +
                            "if (AndroidToken !== null) {" +
                                "resultString = AndroidToken.toString();" +
                            "}" +
                            "window.AndroidApp.receiveString(resultString);" +
                        "})")
            }
        } finally {
            //TODO: Wenn token ein mal übermittelt wurde nicht mehr weitermachen
            if(true) {
                handler?.postDelayed(this, interval)
            }
        }
    }
}