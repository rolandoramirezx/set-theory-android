package com.rolandoramirezx.settheoryandroid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import android.os.AsyncTask
import android.widget.Toast
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.util.LinkedMultiValueMap
import java.net.URI
import android.content.Intent
import com.google.gson.Gson
import java.io.Serializable


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val input : EditText = findViewById(R.id.Calculations)

        //button function
        val calculate : Button = findViewById(R.id.actionButton)
        calculate.setOnClickListener {
            val lines = input.text.toString().split("\n")
            //Then print out each line using Log.v
            lines.forEach { it-> Log.v("Input: ", it)}
            Toast.makeText(applicationContext,
                    "Please wait while we process your request.", Toast.LENGTH_LONG).show();
            HttpRequestTask().execute(*Array(lines.size, { it -> lines[it]}))

        }
    }

    inner class HttpRequestTask : AsyncTask<String, Void, Array<SetResult>>() {
        override fun doInBackground(vararg params: String): Array<SetResult>? {
            val url = "https://set-theory-web.herokuapp.com/api/settheory"
            val restTemplate = RestTemplate()
            restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())

            val map = LinkedMultiValueMap<String, String>()
            map.add("HeaderName", "value");
            map.add("Content-Type", "application/json")
            val entity = HttpEntity<Array<String>>(Array(params.size, {it -> params[it]}), map)

            return restTemplate.exchange(URI(url), HttpMethod.POST, entity, Array<SetResult>::class.java).body
        }

        override fun onPostExecute(setResult: Array<SetResult>) {
            val intent = Intent(this@MainActivity, ResultsActivity::class.java)

            val json = Gson().toJson(setResult)
            intent.putExtra("json", json)
            this@MainActivity.startActivity(intent) //works because innerclasses can reach outterclasses
        }

    }
}

class ResultsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_resultsactivity)

        with (Gson().fromJson(intent.getStringExtra("json"), Array<SetResult>::class.java)){
            findViewById<EditText>(R.id.Calculations).setText(joinToString(separator = "\n") { it -> "${it.set} -> ${it.outcome}" })
        }

        //reference to returnButton
        val actionButton: Button = findViewById(R.id.actionButton)
        actionButton.setOnClickListener{
            val intent = Intent(this@ResultsActivity, MainActivity::class.java)
            this@ResultsActivity.startActivity(intent)
        }

    }
}









data class MessageEntity(var message: String = "") //val = read only

data class SetResult(var set : String = "", var outcome : String = ""): Serializable

