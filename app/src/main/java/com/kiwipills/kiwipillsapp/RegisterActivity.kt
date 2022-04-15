package com.kiwipills.kiwipillsapp

import android.content.Context
import android.os.Bundle
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

    private var context: Context? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        context = this.context

        val btnRegister = findViewById<Button>(R.id.btn_Register)

        val txt_email = findViewById<EditText>(R.id.txt_EmailReg)
        val txt_password = findViewById<EditText>(R.id.txt_PswdReg)
        val txt_user = findViewById<EditText>(R.id.txt_UserReg)
        val txt_name = findViewById<EditText>(R.id.txt_NameReg)
        val txt_lastname1 = findViewById<EditText>(R.id.txt_LastNameReg)
        val txt_lastname2 = findViewById<EditText>(R.id.txt_LastNameMReg)
        val phone = findViewById<EditText>(R.id.txt_PhoneReg)

        btnRegister.setOnClickListener {
            val email = txt_email.text.toString()

            val user = User(email)

            //signin(user)

            Toast.makeText(this, email, Toast.LENGTH_SHORT).show()
        }

    }

    fun signin(userData: User){
        val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<Int> = service.signin(userData)

        result.enqueue(object: Callback<Int> {
            override fun onFailure(call: Call<Int>, t: Throwable) {
                Toast.makeText(context,"Error",Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                Toast.makeText(context,"OK",Toast.LENGTH_LONG).show()
            }
        })
    }
}