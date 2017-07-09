package de.fhbielefeld.braintrainer.javascript

import android.util.Log
import android.webkit.JavascriptInterface

class JSInterface {

    companion object {
        private val TAG = "JSInterface"
    }

    @JavascriptInterface fun receiveString(value: String) {
        Log.d(TAG, "Value from receiveString: $value")
        if(!value.equals("undefined")) {
            // TODO: Hier sendRegistrationToServer aufrufen
        }
    }
}