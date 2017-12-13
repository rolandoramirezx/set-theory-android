package com.rolandoramirezx.settheoryandroid

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val input : EditText = findViewById(R.id.Input)


        val calculate : Button = findViewById(R.id.Calculate)
        calculate.setOnClickListener { Log.v("Hello world", input.text.toString()) }
    }
}
