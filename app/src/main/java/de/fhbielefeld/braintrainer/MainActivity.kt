package de.fhbielefeld.braintrainer

import android.os.AsyncTask
import android.os.Build
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import de.fhbielefeld.braintrainer.periodictask.PeriodicTask
import de.fhbielefeld.braintrainer.network.FirebaseTokenToServer

class MainActivity : AppCompatActivity() {

    companion object {
        private val TAG: String = "MainActivity"
        var idToken: String? = null
        var asyncTask: FirebaseTokenToServer = FirebaseTokenToServer()
        fun runAsyncTask(idToken: String?, firebaseToken: String?) {
            if(asyncTask.status == AsyncTask.Status.FINISHED) {
                asyncTask = FirebaseTokenToServer()
            }
            if(asyncTask.status != AsyncTask.Status.RUNNING) {
                asyncTask.execute(idToken, firebaseToken)
            }
        }
    }

    private var webview: WebView?
    private var handler: Handler?
    private var periodicTask: PeriodicTask?
    private var firePeriodicTask: Boolean = true

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

        readIntentExtras()
    }

    private fun configureWebview(webview: WebView) {
        webview.webViewClient = object:WebViewClient() {
            override fun onPageFinished(view: WebView?, url: String?) {
                Log.d(TAG, getString(R.string.debug_url_loaded))
                if(firePeriodicTask) {
                    view?.let { webView -> periodicTask = PeriodicTask(webView) }
                    periodicTask?.run()
                    firePeriodicTask = false
                }
            }
        }

        webview.settings?.javaScriptEnabled = true
        webview.settings?.domStorageEnabled = true
    }

    private fun loadBraintrainerWebsite(webview: WebView) {
        val url: String?
        if (Build.VERSION.SDK_INT >= 21) {
            webview.settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
            url = getString(R.string.localhost)
        } else {
            url = getString(R.string.braintrainerURL)
        }
        webview.loadUrl(url)
    }

    private fun readIntentExtras() {
        Log.d(TAG, "Extras from the Intent following")
        val bundle: Bundle? = intent.extras
        if(bundle != null) {
            for(key in bundle.keySet()) {
                val value: Any = bundle.get(key)
                Log.d(TAG, "$key $value (${value.javaClass.name})")
            }
            if(bundle.containsKey("ueben")) {
                val temp: Boolean = bundle.getString("ueben").equals("true")
                Log.d(TAG, "ueben: $temp")
            }
        }
    }

    override fun onBackPressed() {
        if(webview != null && webview!!.canGoBack()) {
            webview!!.goBack()
        } else {
            this.finish()
        }

    }
}
