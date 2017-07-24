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

    private val client: OkHttpClient = OkHttpClient()

    override fun doInBackground(vararg tokens: String?): Boolean {
        if(tokens.size == 2) {
            try {
                val profile: Response = client.newCall(createRequest(tokens[0], false, null)).execute()
                val jsonObj = JSONObject(profile.body()?.string())
                jsonObj.put("firebasetoken", tokens[1])

                val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), jsonObj.toString())
                val newProfile: Response = client.newCall(createRequest(tokens[0], true, requestBody)).execute()
            } catch (e: Exception) {
                Log.e(TAG, e.localizedMessage)
                return false
            }
        }
        return true
    }

    private fun createRequest(bearer: String?, put: Boolean, requestBody: RequestBody?): Request {
        val request = Request.Builder()
                .url("https://braintrainer.herokuapp.com/api/profile")
                .addHeader("Authorization", "Bearer $bearer")
        if(put) {
            request.put(requestBody)
        }
        return request.build()
    }
}