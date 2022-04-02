package com.kiwipills.kiwipillsapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnEntrar = findViewById<Button>(R.id.btn_loginLog)
        btnEntrar.setOnClickListener { view ->
            val  activityIntent =  Intent(this,MainActivity::class.java)
            startActivity(activityIntent)
        }
    }
}