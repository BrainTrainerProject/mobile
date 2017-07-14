package de.fhbielefeld.braintrainer.network

import android.os.AsyncTask
import android.util.Log
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.lang.Exception

class FirebaseTokenToServer : AsyncTask<String, Int, Boolean>() {


    companion object {
        private val TAG: String = "FirebaseTokenToServer"
    }

    override fun doInBackground(vararg tokens: String?): Boolean {
        Log.d(TAG, "Variables in varargs following")
        for (x in tokens) {
            Log.d(TAG, x)
        }

        if(tokens.size == 2) {
            val client: OkHttpClient = OkHttpClient()

            val request = Request.Builder()
                    .url("http://10.132.162.241:8080/api/profile")
                    .addHeader("Authorization", "Bearer ${tokens[0]}")
                    .build()

            try {
                val response: Response = client.newCall(request).execute()
                Log.d(TAG, "Responsebody: ${response.body()?.string()}")
            } catch (e: Exception) {
                Log.e(TAG, e.localizedMessage)
            }
        }
        return false
    }

}