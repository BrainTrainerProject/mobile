package de.fhbielefeld.braintrainer.network

import android.os.AsyncTask
import android.util.Log
import okhttp3.*
import org.json.JSONObject
import java.lang.Exception

class FirebaseTokenToServer : AsyncTask<String, Int, Boolean>() {

    companion object {
        private val TAG: String = "FirebaseTokenToServer"
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

    override fun doInBackground(vararg tokens: String?): Boolean {
//        Log.d(TAG, "Variables in varargs following")
        for (x in tokens) {
//            Log.d(TAG, x)
        }

        if(tokens.size == 2) {
            val client: OkHttpClient = OkHttpClient()

            val request = Request.Builder()
                    .url("http://192.168.178.35:8080/api/profile")
                    .addHeader("Authorization", "Bearer ${tokens[0]}")
                    .build()

            try {
                val response: Response = client.newCall(request).execute()
                val body = response.body()?.string()
//                Log.d(TAG, "Response body: ${body}")
                val jsonObj = JSONObject(body)
                jsonObj.put("firebasetoken", tokens[1])
//                Log.d(TAG, jsonObj.toString())

                val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObj.toString())
                val request2 = Request.Builder()
                        .url("http://192.168.178.35:8080/api/profile")
                        .addHeader("Authorization", "Bearer ${tokens[0]}")
                        .put(requestBody)
                        .build()

                val response2: Response = client.newCall(request2).execute()
//                Log.d(TAG, "Response body2: ${response2?.body()?.string()}")
            } catch (e: Exception) {
                Log.e(TAG, e.localizedMessage)
                return false
            }
        }
        return true
    }

}