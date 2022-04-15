package com.kiwipills.kiwipillsapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

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
            R.id.opc_list->{
                this.intSelection =  1
                val activityIntent = Intent(this,MainActivity::class.java)
                startActivity(activityIntent)
            }
            R.id.opc_new->{

                this.intSelection = 2
                val activityIntent = Intent(this,NewMedsActivity::class.java)
                startActivity(activityIntent)
            }
        }

        //Cierra las opciones de menu
        if(drawer.isDrawerOpen(GravityCompat.START)){
            drawer.closeDrawer(GravityCompat.START)
        }
        return true
    }
}