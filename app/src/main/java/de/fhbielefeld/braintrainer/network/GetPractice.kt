package de.fhbielefeld.braintrainer.network

import android.os.AsyncTask
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import de.fhbielefeld.braintrainer.MainActivity
import de.fhbielefeld.braintrainer.R
import de.fhbielefeld.braintrainer.TrainActivity
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.lang.Exception

class GetPractice(question: TextView, answer: EditText, next: Button, activity: TrainActivity): AsyncTask<Void, Int, Boolean>() {

    companion object {
        private val TAG: String = "GetPractice"
    }

    private var question: TextView? = question
    private var answer: EditText? = answer
    private var next: Button? = next
    private val activity: TrainActivity = activity

    private val client: OkHttpClient = OkHttpClient()

    private var notecardIndex: Int = 0
    private var notecards: ArrayList<JSONObject> = ArrayList<JSONObject>()

    override fun doInBackground(vararg params: Void?): Boolean {
        while(MainActivity.idToken == null) {
            Thread.sleep(1000)
            // Waiting for PeriodicTask to set idToken in MainActivity
        }

        try {
            val practice: Response = client.newCall(createRequest("practice")).execute()
            val jsonArr = JSONArray(practice.body()?.string())
            repeat(jsonArr.length()) { i ->
                val notecard: Response = client.newCall(createRequest("notecard/${jsonArr[i]}")).execute()
                val jsonObj = JSONObject(notecard.body()?.string())
                notecards.add(jsonObj)
            }
        } catch (e: Exception) {
            Log.e(TAG, e.localizedMessage)
            return false
        }
        return true
    }

    override fun onPostExecute(result: Boolean?) {
        if(result == true && notecards.size > 0) {
            setUI(0)
            next!!.setOnClickListener(object:View.OnClickListener {
                override fun onClick(view: View?) {
                    checkAnswer()
                    if(notecardIndex == notecards.size - 1){
                        question!!.text = "Du hast ${countCorrect()} von ${notecards.size} korrekt beantwortet"
                        answer!!.visibility = View.INVISIBLE
                        view!!.setOnClickListener(object:View.OnClickListener {
                            override fun onClick(p0: View?) {
                                SendEvaluatePractice(notecards,
                                        activity.resources.getString(R.string.apiAddress),
                                        this@GetPractice).execute()
                            }
                        })
                    } else {
                        setUI(++notecardIndex)
                    }
                }
            })
        } else {
            quit()
        }
    }

    private fun createRequest(url: String): Request {
        return Request.Builder()
                .url("${activity.resources.getString(R.string.apiAddress)}$url")
                .addHeader("Authorization", "Bearer ${MainActivity.idToken}")
                .build()
    }

    private fun checkAnswer() {
        val value: Boolean = notecards[notecardIndex].getString("answer") == answer!!.text.toString()
        notecards[notecardIndex].put("success", value)
    }

    private fun countCorrect(): Int {
        var count: Int = 0
        for(notecard in notecards) {
            if(notecard.has("success")) {
                if(notecard.getBoolean("success") == true) {
                    count++
                }
            }
        }
        return count
    }

    private fun setUI(notecardToSet: Int){
        question!!.text = notecards[notecardToSet].getString("task")
        answer!!.setText("")
    }

    fun quit() {
        question = null
        answer = null
        next = null
        activity.finish()
    }
}
