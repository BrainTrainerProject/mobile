package de.fhbielefeld.braintrainer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import de.fhbielefeld.braintrainer.network.GetPractice

class TrainActivity : AppCompatActivity() {

    companion object {
        private val TAG: String = "TrainActivity"
    }

    private var question: TextView? = null
    private var answer: EditText? = null
    private var next: Button? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_train)

        question = findViewById(R.id.question)
        answer = findViewById(R.id.answer)
        next = findViewById(R.id.next)

        var getPractice: GetPractice = GetPractice(question!!, answer!!, next!!, this)
        getPractice.execute()
    }
}
