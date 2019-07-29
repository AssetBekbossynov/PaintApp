package com.example.arcorefirstapp

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_start.*

class StartActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_start)

        var intent: Intent

        startApp.setOnClickListener {
            when(options.checkedRadioButtonId){
                R.id.arCore -> {
                    intent = Intent(applicationContext, MainActivity::class.java)
                    startActivity(intent)
                }
                R.id.textRec -> {
                    intent = Intent(applicationContext, TextRecognitionActivity::class.java)
                    startActivity(intent)
                }
                else ->{
                    Toast.makeText(this, "Nothing selected", Toast.LENGTH_SHORT).show()
                }
            }

        }
    }
}
