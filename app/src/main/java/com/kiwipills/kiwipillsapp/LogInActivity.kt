package com.kiwipills.kiwipillsapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton

class LogInActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val btnLogin = findViewById<Button>(R.id.btn_loginLog)
        val btnRegister = findViewById<TextView>(R.id.txt_registerLog)


        btnLogin.setOnClickListener { view ->
            val activityIntent = Intent(this,MainActivity::class.java)
            startActivity(activityIntent)
        }

        //Abrir ventana de registro
        btnRegister.setOnClickListener {
            val activityIntent = Intent(this,RegisterActivity::class.java)
            startActivity(activityIntent)
        }
    }
}