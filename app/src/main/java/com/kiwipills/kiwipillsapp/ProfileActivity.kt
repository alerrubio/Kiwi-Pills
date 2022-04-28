package com.kiwipills.kiwipillsapp

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.kiwipills.kiwipillsapp.Utils.Globals


class ProfileActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        val toolbar = findViewById<Toolbar>(R.id.toolbarprofile)
        setSupportActionBar(toolbar)
        toolbar.setTitle("Perfil")

        var actionBar = getSupportActionBar()

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        val txt_name = findViewById<EditText>(R.id.txt_name_prof)
        val txt_lastname1 = findViewById<EditText>(R.id.txt_lastname1_prof)
        val txt_lastname2 = findViewById<EditText>(R.id.txt_lastname2_prof)
        val txt_email = findViewById<EditText>(R.id.txt_email_prof)
        val txt_phone = findViewById<EditText>(R.id.txt_phone_prof)

        val btn_logout = findViewById<Button>(R.id.btn_profile_logout)

        if(Globals.DB){
            txt_name.setText(Globals.UserLogged.name)
            txt_lastname1.setText(Globals.UserLogged.lastname01)
            txt_lastname2.setText(Globals.UserLogged.lastname02)
            txt_email.setText(Globals.UserLogged.email)
            txt_phone.setText(Globals.UserLogged.phone)
        }

        btn_logout.setOnClickListener {
            finish();
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

}