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

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG: String = "MainActivity"
        var idToken: String? = null
    }

    private var webview: WebView? = null
    private var periodicTask: PeriodicTask? = null
    private var firePeriodicTask: Boolean = true

    private var ueben: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        readIntentExtras()

        webview = findViewById<WebView>(R.id.webview)
        configureWebview(webview)
        loadBraintrainerWebsite(webview)
    }

    private fun configureWebview(webview: WebView?) {
        webview?.webViewClient = object:WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                if(firePeriodicTask) {
                    view?.let { webView -> periodicTask = PeriodicTask(webView) }
                    periodicTask?.run()
                    firePeriodicTask = false
                }
                triggerUebung()
            }
        }

        webview?.settings?.javaScriptEnabled = true
        webview?.settings?.domStorageEnabled = true
    }

    private fun triggerUebung() {
        if(ueben) {
            Handler().postDelayed({
                loadPracticeWebsite(webview)
                ueben = false
            }, 1000)
        }
    }

    private fun loadBraintrainerWebsite(webview: WebView?) {
        val url: String
        if (Build.VERSION.SDK_INT >= 21) {
            webview?.settings?.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
            url = getString(R.string.braintrainerSecureURL)
        } else {
            url = getString(R.string.braintrainerURL)
        }
        webview?.loadUrl(url)
    }

    private fun loadPracticeWebsite(webview: WebView?) {
        val url: String
        if (Build.VERSION.SDK_INT >= 21) {
            url = getString(R.string.practiceSecureURL)
        } else {
            url = getString(R.string.practiceURL)
        }
        webview?.loadUrl(url)
    }

    private fun readIntentExtras() {
        val bundle: Bundle? = intent.extras
        if(bundle != null) {
            if(bundle.containsKey("ueben") && bundle.getString("ueben") == "true") {
                ueben = true
            }
        }
    }

    override fun onBackPressed() {
        if(webview != null && webview!!.canGoBack()) {
            webview?.goBack()
        } else {
            super.onBackPressed()
        }

    }
}
