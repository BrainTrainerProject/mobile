package de.fhbielefeld.braintrainer

import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView

class MainActivity : AppCompatActivity() {

    private var webview = findViewById<WebView>(R.id.webview)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        webview.settings.javaScriptEnabled = true
        if (Build.VERSION.SDK_INT >= 21) {
            webview.settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
            webview.loadUrl(getString(R.string.braintrainerSecureURL))
        } else {
            webview.loadUrl(getString(R.string.braintrainerURL))
        }
    }
}
