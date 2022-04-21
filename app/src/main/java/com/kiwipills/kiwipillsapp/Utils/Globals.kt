package com.kiwipills.kiwipillsapp.Utils

import android.app.Application
import com.kiwipills.kiwipillsapp.service.Models.User

class Globals: Application()  {
    companion object{
        //Objeto que guarda al usuario logueado
        lateinit var UserLogged: User

        val DB: Boolean = true
    }
}