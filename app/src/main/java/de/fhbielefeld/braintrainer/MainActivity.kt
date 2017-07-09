package de.fhbielefeld.braintrainer

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import de.fhbielefeld.braintrainer.periodictask.PeriodicTask
import de.fhbielefeld.braintrainer.javascript.JSInterface

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG = "MainActivity"
        val idToken = "something"
    }

    private var webview: WebView?
    private var handler: Handler?
    private var periodicTask: PeriodicTask?

    init {
        webview = null
        handler = Handler()
        periodicTask = null
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webview = findViewById<WebView>(R.id.webview)

        webview?.let { webView -> configureWebview(webView) }

        webview?.let { webView -> loadBraintrainerWebsite(webView) }
    }

    private fun configureWebview(webview: WebView) {
        webview.webViewClient = object:WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                Log.d(TAG, "URL has been loaded!")
                view?.let { view -> periodicTask = PeriodicTask(view) }
                periodicTask?.run()
            }
        }

        webview.settings?.javaScriptEnabled = true
        webview.settings?.domStorageEnabled = true
        webview.addJavascriptInterface(JSInterface(), "AndroidApp")
    }

    private fun loadBraintrainerWebsite(webview: WebView) {
        var url: String?
        if (Build.VERSION.SDK_INT >= 21) {
            webview.settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
            url = getString(R.string.braintrainerSecureURL)
        } else {
            url = getString(R.string.braintrainerURL)
        }
        webview.loadUrl(url)
    }
}
