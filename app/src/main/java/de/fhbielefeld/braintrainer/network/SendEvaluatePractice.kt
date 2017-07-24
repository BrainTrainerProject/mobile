package de.fhbielefeld.braintrainer.network

import android.os.AsyncTask
import android.util.Log
import de.fhbielefeld.braintrainer.MainActivity
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject

class SendEvaluatePractice(notecards: ArrayList<JSONObject>, apiAddress: String, getPractice: GetPractice): AsyncTask<Void, Int, Boolean>() {

    companion object {
        private val TAG: String = "SendEvaluatePractice"
    }

    private var notecards: ArrayList<JSONObject>? = notecards
    private val apiAddress: String = apiAddress
    private val getPractice: GetPractice? = getPractice

    private val client: OkHttpClient = OkHttpClient()

    override fun doInBackground(vararg p0: Void?): Boolean {
        if(notecards!!.size > 0) {
            val evaluations: JSONArray = JSONArray()
            repeat(notecards!!.size) { i ->
                val jsonObj: JSONObject = JSONObject()
                jsonObj.put("notecard", notecards!![i].getString("_id"))
                jsonObj.put("success", notecards!![i].getBoolean("success"))
                evaluations.put(jsonObj)
            }
            Log.d(TAG, evaluations.toString())
            val requestBody: RequestBody = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), evaluations.toString())
            val request: Request = Request.Builder()
                    .url("${apiAddress}practice/evaluate")
                    .addHeader("Authorization", "Bearer ${MainActivity.idToken}")
                    .put(requestBody)
                    .build()
            try {
                val response: Response = client.newCall(request).execute()
                if(response.code() != 200) {
                    return false
                }
            } catch (e: Exception) {
                Log.e(TAG, e.localizedMessage)
                return false
            }
        }
        return true
    }

    override fun onPostExecute(result: Boolean?) {
        notecards = null
        getPractice!!.quit()
    }
}