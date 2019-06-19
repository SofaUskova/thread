package com.example.tread.activities

import android.os.Bundle
import android.os.PersistableBundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.tread.R
import com.example.tread.async.AsyncTaskOne

class MainActivity : AppCompatActivity() {
    private var taskOne: AsyncTaskOne? = null
    lateinit var textView: TextView
    lateinit var buttonClick: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.example.tread.R.layout.activity_main)

        val editText = findViewById<EditText>(R.id.editText)
        buttonClick = findViewById(R.id.button)
        textView = findViewById(R.id.textView)

        if (savedInstanceState != null) {
            buttonClick.isEnabled = savedInstanceState.getBoolean("BUTTON")
        }

        taskOne = lastCustomNonConfigurationInstance as AsyncTaskOne?
        if (taskOne == null) {
            taskOne = AsyncTaskOne(editText)
        }
        taskOne?.link(this)

        buttonClick.setOnClickListener {
            try {
                taskOne?.execute()
            } catch (e: Exception) {
                Toast.makeText(this, "Async уже был запущен", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle?, outPersistentState: PersistableBundle?) {
        taskOne?.unLink()
        outState?.putBoolean("BUTTON", buttonClick.isEnabled)
        val saved = onRetainCustomNonConfigurationInstance() as AsyncTaskOne?
    }

    override fun onRetainCustomNonConfigurationInstance(): Any? {
        return taskOne
    }
}
