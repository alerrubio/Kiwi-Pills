package com.kiwipills.kiwipillsapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kiwipills.kiwipillsapp.Utils.Globals
import com.kiwipills.kiwipillsapp.service.Models.User
import com.kiwipills.kiwipillsapp.service.RestEngine
import com.kiwipills.kiwipillsapp.service.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val btnRegister = findViewById<Button>(R.id.btn_Register)

        val txt_email = findViewById<EditText>(R.id.txt_EmailReg)
        val txt_password = findViewById<EditText>(R.id.txt_PswdReg)
        val txt_username = findViewById<EditText>(R.id.txt_UserReg)
        val txt_name = findViewById<EditText>(R.id.txt_NameReg)
        val txt_lastname01 = findViewById<EditText>(R.id.txt_LastNameReg)
        val txt_lastname02 = findViewById<EditText>(R.id.txt_LastNameMReg)
        val txt_phone = findViewById<EditText>(R.id.txt_PhoneReg)

        btnRegister.setOnClickListener {
            val email = txt_email.text.toString()
            val password = txt_password.text.toString()
            val username = txt_username.text.toString()
            val name = txt_name.text.toString()
            val lastname01 = txt_lastname01.text.toString()
            val lastname02 = txt_lastname02.text.toString()
            val phone = txt_phone.text.toString()

            val user = User(0, email, password, username, name, lastname01, lastname02, phone)

            if(Globals.DB){
                if(checkfields(email, password, username)){

                    signup(user)
                }
            }
        }

    }


    fun signup(userData: User){

        val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<User> = service.signup(userData)

        result.enqueue(object: Callback<User> {
            override fun onFailure(call: Call<User>, t: Throwable) {
                Toast.makeText(this@RegisterActivity,"No se pudo realizar registro",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<User>, response: Response<User>) {

                Globals.UserLogged = response.body()!!
                Log.d("Usuario logueado: ", Globals.UserLogged.toString())
                Toast.makeText(this@RegisterActivity, Globals.UserLogged.username, Toast.LENGTH_LONG).show()

                val activityIntent = Intent(this@RegisterActivity,MainActivity::class.java)
                startActivity(activityIntent)
                finish()
            }
        })
    }

    fun checkfields(email: String, password: String, username: String) : Boolean {
        if(email == ""){
            Toast.makeText(this@RegisterActivity, "Correo requerido", Toast.LENGTH_SHORT).show()
            return false
        }
        if(password == ""){
            Toast.makeText(this@RegisterActivity, "Contraseña requerida", Toast.LENGTH_SHORT).show()
            return false
        }
        if(username == ""){
            Toast.makeText(this@RegisterActivity, "Usuario requerido", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

}