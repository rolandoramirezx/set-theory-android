package com.rolandoramirezx.settheoryandroid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.web.client.RestTemplate
import android.os.AsyncTask



class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val input : EditText = findViewById(R.id.Input)


        val calculate : Button = findViewById(R.id.Calculate)
        calculate.setOnClickListener {
            //Log.v("Hello world", input.text.toString())
            //TODO-Split the string by lines
            val lines = input.text.toString().split("\n")
            //Then print out each line using Log.v
            lines.forEach { it-> Log.v("Input: ", it)}
            HttpRequestTask().execute()

        }
    }
}

private class HttpRequestTask : AsyncTask<Void, Void, MessageEntity>() {
    override fun doInBackground(vararg params: Void): MessageEntity? {
    val url = "https://set-theory-web.herokuapp.com/api/settheory"
        val restTemplate = RestTemplate()
        restTemplate.messageConverters.add(MappingJackson2HttpMessageConverter())
        return restTemplate.getForObject(url, MessageEntity::class.java)
    }

    override fun onPostExecute(messageEntity: MessageEntity) {
    Log.v("output", messageEntity.toString())
    }

}

data class MessageEntity(var message: String = "") //val = read only
