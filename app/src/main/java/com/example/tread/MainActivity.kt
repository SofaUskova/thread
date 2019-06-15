package com.example.tread

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    var array: MutableList<String> = mutableListOf("zero")
    private lateinit var treadFour: Thread
    private lateinit var handler: Handler

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val textView = findViewById<TextView>(R.id.textView)
        val buttonClick = findViewById<Button>(R.id.button)
        var count = 0

        handler =
            @SuppressLint("HandlerLeak")
            object : Handler() {

                override fun handleMessage(msg: android.os.Message) {
                    if (msg.obj != null)
                        textView.text = msg.obj.toString()
                    if (msg.what == 1)
                        buttonClick.isEnabled = true
                }
            }

        buttonClick.setOnClickListener {
            buttonClick.isEnabled = false

            Thread(Runnable {
                createArray()
                for (i in 0 until array.size) {
                    setMessage(0, array[i])
                    Thread.sleep(100)
                }
                cleanArray()
            }).start()

            /*аналог wait? Как это реализовать в Kotlin*/
            treadFour = Thread(Runnable {
                setMessage(0, "Yup!")
            })

            Thread(Runnable {
                for (j in 2..100) {
                    for (i in 2..100) {
                        if (j % i == 0)
                            count++
                        Thread.sleep(50)
                    }
                    if (count == 1) {

                        setMessage(0, j.toString())
                        Thread.sleep(100)

                        /*будит поток, как это реализовать в Kotlin?*/
                        treadFour.start()
                        count = 0
                    }
                }
            }).start()

            var i = 1
            Thread(Runnable {
                setMessage(0, i.toString())
                if (i == 10) {
                    //тут все потоки должны выйти (непоняла)
                    //используя volatile переменную
                    //ждет пока завершаться
                    handler.sendEmptyMessage(1)
                }
                i++
                Thread.sleep(5000)
            }).start()
        }
    }

    @Synchronized
    fun setMessage(int: Int, str: String) {
        val msg = handler.obtainMessage(int, str)
        handler.sendMessage(msg)
    }

    @Synchronized
    fun createArray() {
        array = arrayListOf("one", "two", "three", "four", "five", "six", "seven")
    }

    @Synchronized
    fun cleanArray() {
        array.clear()
    }
}
