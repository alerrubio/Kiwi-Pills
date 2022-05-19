package com.kiwipills.kiwipillsapp

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.kiwipills.kiwipillsapp.Utils.Globals
import com.kiwipills.kiwipillsapp.Utils.ImageUtilities
import com.kiwipills.kiwipillsapp.service.Models.Medicament
import com.kiwipills.kiwipillsapp.service.Models.User
import com.kiwipills.kiwipillsapp.service.RestEngine
import com.kiwipills.kiwipillsapp.service.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*


class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {
    private val adapter by lazy{ ViewPagerAdapater(this)
    }

    private var intSelection:Int =  0
    lateinit var toolbar:Toolbar
    lateinit var drawer:DrawerLayout

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //drawer
        drawer = findViewById<DrawerLayout>(R.id.elDrawer)
        toolbar = findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name)

        drawer.addDrawerListener(toggle)
        toggle.syncState()

        val navigationView: NavigationView = findViewById(R.id.nav)

        navigationView.setNavigationItemSelectedListener(this)

        //Obtener medicamentos de usuario
        //getMedicaments()

        //Cambiar header de usuario logueado
        if(Globals.DB){
            val header = navigationView.getHeaderView(0)

            val usernameTitle = header.findViewById<TextView>(R.id.lbl_username_drawer)
            val userImage = header.findViewById<ImageView>(R.id.iv_userImage_headerr)
            //Usuario
            usernameTitle.text = Globals.UserLogged.username

            Toast.makeText(this@MainActivity, Globals.UserLogged.username, Toast.LENGTH_LONG).show()

            //Si existe imagen de usuario
            if(Globals.UserLogged.image != ""){
                //Imagen de usuario
                var byteArray:ByteArray? = null
                val strImage:String = Globals.UserLogged.image!!.replace("data:image/png;base64,","")
                byteArray =  Base64.getDecoder().decode(strImage)
                userImage.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))
            }

        }

        //nav tablelayout
        val pagerMain =  findViewById<ViewPager2>(R.id.pager)
        pagerMain.adapter =  this.adapter

        val tab_layoutMain =  findViewById<TabLayout>(R.id.tab_layout)

        //Aqui ya sabe quien es nuestro tab, y quien nuestro pager
        val tabLayoutMediator =  TabLayoutMediator(tab_layoutMain,pagerMain
            , TabLayoutMediator.TabConfigurationStrategy { tab, position ->
                when(position){
                    0-> {
                        tab.text =  "Inicio"
                        tab.setIcon(R.drawable.ic_home_24)
                    }
                    1-> {
                        tab.text =  "Mis medicamentos"
                        tab.setIcon(R.drawable.ic_medicine)
                    }
                    2-> {
                        tab.text =  "Caja de píldoras"
                        tab.setIcon(R.drawable.ic_today_24)
                    }
                }
            })
        tabLayoutMediator.attach()

    }

    fun cambiarFragmento(fragmentoNuevo: Fragment, tag: String){

        val fragmentoAnterior = supportFragmentManager.findFragmentByTag(tag)
        if(fragmentoAnterior == null){

            supportFragmentManager.beginTransaction().replace(R.id.contenedor, fragmentoNuevo).commit()

        }

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.opc_profile->{
                this.intSelection =  0
                val activityIntent = Intent(this,ProfileActivity::class.java)
                startActivity(activityIntent)

            }
            R.id.opc_new->{
                this.intSelection = 1
                val activityIntent = Intent(this,NewMedsActivity::class.java)
                startActivity(activityIntent)
            }
            R.id.opc_logout->{
                this.intSelection = 2
                val activityIntent = Intent(this,LogInActivity::class.java)
                Globals.UserLogged = User()
                startActivity(activityIntent)
                finish()
            }
            /*R.id.opc_borradores->{
                this.intSelection = 3
                val activityIntent = Intent(this,MainActivity::class.java)
                Globals.UserLogged = User()
                startActivity(activityIntent)
                finish()
            }*/
        }

        //Cierra las opciones de menu
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START)
        }
        return true
    }

    private fun getMedicaments(){
        val user_id = Globals.UserLogged.id!!
        val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<List<Medicament>> = service.getMedicaments(user_id)

        result.enqueue(object: Callback<List<Medicament>> {

            override fun onFailure(call: Call<List<Medicament>>, t: Throwable){
                Toast.makeText(this@MainActivity,"Error", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Medicament>>, response: Response<List<Medicament>>){
                val arrayItems =  response.body()
                if (arrayItems != null){
                    for (item in arrayItems){
                        Log.d("Medicamento: ", item.toString())
                    }
                }
                Toast.makeText(this@MainActivity,"Medicamentos obtenidos", Toast.LENGTH_LONG).show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        if(Globals.DB){

            val navigationView: NavigationView = findViewById(R.id.nav)
            val header = navigationView.getHeaderView(0)
            val usernameTitle = header.findViewById<TextView>(R.id.lbl_username_drawer)
            val userImage = header.findViewById<ImageView>(R.id.iv_userImage_headerr)

            usernameTitle.text = Globals.UserLogged.username

            //Si existe imagen de usuario
            if(Globals.UserLogged.image != ""){
                //Imagen de usuario
                var byteArray:ByteArray? = null
                val strImage:String = Globals.UserLogged.image!!.replace("data:image/png;base64,","")
                byteArray =  Base64.getDecoder().decode(strImage)
                userImage.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))
            }
        }
    }

}