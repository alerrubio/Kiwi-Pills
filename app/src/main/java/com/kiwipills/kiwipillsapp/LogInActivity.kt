
package com.kiwipills.kiwipillsapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.Preference
import android.preference.PreferenceManager
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

        //obtener preferencias
        val prefs = PreferenceManager.getDefaultSharedPreferences(this)

        val btnLogin = findViewById<Button>(R.id.btn_loginLog)
        val btnRegister = findViewById<TextView>(R.id.txt_registerLog)
        val txtEmail = findViewById<TextView>(R.id.txt_emailLog)
        val txtPassword = findViewById<TextView>(R.id.txt_passwordLog)

        //recuperar preferencias
        val session = prefs.getBoolean("session", false)
        if(session){
            recoverPrefers(prefs)
            val activityIntent = Intent(this,MainActivity::class.java)
            startActivity(activityIntent)
            finish()
        }

        btnLogin.setOnClickListener { view ->
            val email = txtEmail.text.toString()
            val password = txtPassword.text.toString()

            //borrar
            //edit.remove(key)
            //edit.apply()

            if(Globals.DB){
                login(prefs,email, password)
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

    fun login(prefs:SharedPreferences, email: String, password: String){
            val userData = User(0,email,password)

            val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
            val result: Call<User> = service.login(userData)

            result.enqueue(object: Callback<User> {
                override fun onFailure(call: Call<User>, t: Throwable) {
                    Toast.makeText(this@LogInActivity,"Credenciales incorrectas", Toast.LENGTH_SHORT).show()
                }

                override fun onResponse(call: Call<User>, response: Response<User>) {

                    Globals.UserLogged = response.body()!!
                    updatePrefers(prefs)

                    val activityIntent = Intent(this@LogInActivity,MainActivity::class.java)
                    startActivity(activityIntent)
                }
            })

        }

        fun updatePrefers(prefs:SharedPreferences){
            val edit = prefs.edit()
            edit.putBoolean("session", true)
            edit.putInt("id", Globals.UserLogged.id!!)
            edit.putString("email", Globals.UserLogged.email)
            edit.putString("username", Globals.UserLogged.username)
            edit.putString("password", Globals.UserLogged.password)
            edit.putString("name", Globals.UserLogged.name)
            edit.putString("lastname01", Globals.UserLogged.lastname01)
            edit.putString("lastname02", Globals.UserLogged.lastname02)
            edit.putString("phone", Globals.UserLogged.phone)
            edit.putString("image", Globals.UserLogged.image)

            edit.apply()
        }

        fun recoverPrefers(prefs:SharedPreferences){
            Globals.UserLogged = User()
            Globals.UserLogged.id = prefs.getInt("id", 0)
            Globals.UserLogged.email = prefs.getString("email", "")
            Globals.UserLogged.password = prefs.getString("password", "")
            Globals.UserLogged.username = prefs.getString("username", "")
            Globals.UserLogged.name = prefs.getString("name", "")
            Globals.UserLogged.lastname01 = prefs.getString("lastname01", "")
            Globals.UserLogged.lastname02 = prefs.getString("lastname02", "")
            Globals.UserLogged.phone = prefs.getString("phone", "")
            Globals.UserLogged.image = prefs.getString("image", "")

            Log.e("Global user", Globals.UserLogged.toString())

        }

    }