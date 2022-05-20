package com.kiwipills.kiwipillsapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kiwipills.kiwipillsapp.Utils.Globals
import com.kiwipills.kiwipillsapp.data.DataDBHelper
import com.kiwipills.kiwipillsapp.service.Models.User
import com.kiwipills.kiwipillsapp.service.RestEngine
import com.kiwipills.kiwipillsapp.service.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LogInActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)


        val btnLogin = findViewById<Button>(R.id.btn_loginLog)
        val btnRegister = findViewById<TextView>(R.id.txt_registerLog)
        val txtEmail = findViewById<TextView>(R.id.txt_emailLog)
        val txtPassword = findViewById<TextView>(R.id.txt_passwordLog)

        btnLogin.setOnClickListener { view ->
            //val email = txtEmail.text.toString()
            //val password = txtPassword.text.toString()
            val email = "eliseo1677@hotmail.com"
            val password = "123"

            if(Globals.DB){
                login(email, password)
            }else{
                val activityIntent = Intent(this,MainActivity::class.java)
                startActivity(activityIntent)
            }
        }

        //Abrir ventana de registro
        btnRegister.setOnClickListener {
            val activityIntent = Intent(this,RegisterActivity::class.java)
            startActivity(activityIntent)
        }
    }

    fun login(email: String, password: String){
        val userData = User(0,email,password)

        val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<User> = service.login(userData)

        result.enqueue(object: Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@LogInActivity,"Credenciales incorrectas", Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {

                Globals.UserLogged = response.body()!!
                val activityIntent = Intent(this@LogInActivity,MainActivity::class.java)
                startActivity(activityIntent)
            }
        })

    }
}