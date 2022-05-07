package com.kiwipills.kiwipillsapp

import android.os.Bundle
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.kiwipills.kiwipillsapp.Utils.Globals
import com.kiwipills.kiwipillsapp.Utils.ImageUtilities
import java.util.*


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

        val iv_userImage = findViewById<ImageView>(R.id.iv_userImage_prof)
        val txt_username = findViewById<TextView>(R.id.lbl_username_prof)
        val txt_name = findViewById<EditText>(R.id.txt_name_prof)
        val txt_lastname1 = findViewById<EditText>(R.id.txt_lastname1_prof)
        val txt_lastname2 = findViewById<EditText>(R.id.txt_lastname2_prof)
        val txt_email = findViewById<EditText>(R.id.txt_email_prof)
        val txt_phone = findViewById<EditText>(R.id.txt_phone_prof)

        val btn_logout = findViewById<Button>(R.id.btn_profile_logout)

        if(Globals.DB){
            txt_username.setText(Globals.UserLogged.username)
            txt_name.setText(Globals.UserLogged.name)
            txt_lastname1.setText(Globals.UserLogged.lastname01)
            txt_lastname2.setText(Globals.UserLogged.lastname02)
            txt_email.setText(Globals.UserLogged.email)
            txt_phone.setText(Globals.UserLogged.phone)

            //Imagen de usuario
            var byteArray:ByteArray? = null
            val strImage:String = Globals.UserLogged.image!!.replace("data:image/png;base64,","")
            byteArray =  Base64.getDecoder().decode(strImage)
            iv_userImage.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))
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