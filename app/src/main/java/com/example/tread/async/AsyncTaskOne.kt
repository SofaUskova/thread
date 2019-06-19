package com.example.tread.async

import android.annotation.SuppressLint
import android.os.AsyncTask
import android.widget.EditText
import com.example.tread.activities.MainActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class AsyncTaskOne(
    var editText: EditText
) : AsyncTask<String?, String?, String?>() {
    @SuppressLint("StaticFieldLeak")
    var activity: MainActivity? = null

    fun link(act: MainActivity) {
        activity = act
    }

    fun unLink() {
        activity = null
    }

    override fun onPreExecute() {
        activity?.buttonClick?.isEnabled = false
    }

    override fun doInBackground(vararg params: String?): String? {
        val url = URL("http://pub.zame-dev.org/senla-training-addition/lesson-19.php?param=${editText.text}")

        val conn = url.openConnection() as HttpURLConnection
        conn.connect()
        val buffer = StringBuilder()

        try {

            val reader = BufferedReader( InputStreamReader( conn.inputStream ) )

            var line = reader.readLine()

            while (line != null) {
                buffer.append(line + "\n")
                publishProgress(line)
                Thread.sleep(1000)
                line = reader.readLine()
            }

            reader.close()
        } catch (e: Exception) {
            e.message
        } finally {
            conn.disconnect()
        }

        return buffer.toString()
    }

    override fun onPostExecute(result: String?) {
        activity?.textView?.text = result
        activity?.buttonClick?.isEnabled = true

    }

    override fun onProgressUpdate(vararg values: String?) {
        activity?.textView?.text = values[0]
    }

}