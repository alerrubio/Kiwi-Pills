package com.kiwipills.kiwipillsapp

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kiwipills.kiwipillsapp.service.Models.User
import com.kiwipills.kiwipillsapp.service.RestEngine
import com.kiwipills.kiwipillsapp.service.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterActivity : AppCompatActivity() {

    private lateinit var contexto: Context

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        contexto = this

        val btnRegister = findViewById<Button>(R.id.btn_Register)

        val txt_email = findViewById<EditText>(R.id.txt_EmailReg)
        val txt_password = findViewById<EditText>(R.id.txt_PswdReg)
        val txt_username = findViewById<EditText>(R.id.txt_UserReg)
        val txt_name = findViewById<EditText>(R.id.txt_NameReg)
        val txt_lastname1 = findViewById<EditText>(R.id.txt_LastNameReg)
        val txt_lastname2 = findViewById<EditText>(R.id.txt_LastNameMReg)
        val phone = findViewById<EditText>(R.id.txt_PhoneReg)

        btnRegister.setOnClickListener {
            val email = txt_email.text.toString()
            val password = txt_password.text.toString()
            val username = txt_username.text.toString()

            val user = User(0, email, password, username)

            signup(user)
        }

    }


    fun signup(userData: User){
        val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<User> = service.signup(userData)

        result.enqueue(object: Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(contexto,"No se pudo realizar registro",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {

                val auxUser: User? = response.body()
                Log.d("Mi resultado", auxUser.toString())
                Toast.makeText(contexto,"ok",Toast.LENGTH_LONG).show()
            }
        })
    }

}