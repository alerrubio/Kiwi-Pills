package com.kiwipills.kiwipillsapp.Utils

import android.app.Application
import android.util.Log
import com.kiwipills.kiwipillsapp.data.DataDBHelper
import com.kiwipills.kiwipillsapp.service.Models.User

class Globals: Application()  {

    companion object{
        //Objeto que guarda al usuario logueado
        lateinit var UserLogged: User

        lateinit var dbHelper: DataDBHelper

        val DB: Boolean = true
    }

    override fun onCreate() {
        super.onCreate()

        dbHelper = DataDBHelper(applicationContext)
    }


}