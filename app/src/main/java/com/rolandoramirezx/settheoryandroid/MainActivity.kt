package com.rolandoramirezx.settheoryandroid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import android.os.AsyncTask
import org.springframework.http.HttpEntity
import org.springframework.http.HttpMethod
import org.springframework.util.LinkedMultiValueMap
import org.springframework.util.MultiValueMap
import java.net.URI


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val input : EditText = findViewById(R.id.Input)

        //button function
        val calculate : Button = findViewById(R.id.Calculate)
        calculate.setOnClickListener {
            //Log.v("Hello world", input.text.toString())
            //TODO-Split the string by lines
            val lines = input.text.toString().split("\n")
            //Then print out each line using Log.v
            lines.forEach { it-> Log.v("Input: ", it)}
            HttpRequestTask().execute(*Array(lines.size, { it -> lines[it]}))

        }
    }
}

private class HttpRequestTask : AsyncTask<String, Void, Array<SetResult>>() {
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
        Log.v("output", setResult.toString())
    }

}

data class MessageEntity(var message: String = "") //val = read only

data class SetResult(var set : String = "", var outcome : String = "")

