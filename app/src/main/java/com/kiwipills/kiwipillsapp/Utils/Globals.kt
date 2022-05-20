package com.kiwipills.kiwipillsapp.Utils

import android.app.Application
import com.kiwipills.kiwipillsapp.service.Models.Medicament
import com.kiwipills.kiwipillsapp.service.Models.User

class Globals: Application()  {
    companion object{
        //Objeto que guarda al usuario logueado
        lateinit var UserLogged: User
        lateinit var currMedicine: Medicament
        val DB: Boolean = true
    }
}