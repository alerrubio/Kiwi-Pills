package com.kiwipills.kiwipillsapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {
    private val adapter by lazy{ ViewPagerAdapater(this)}
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //drawer
        /*val drawer = findViewById<DrawerLayout>(R.id.elDrawer)
        val toolbar = findViewById<Toolbar>(R.id.toolbar)

        setSupportActionBar(toolbar)
        val toggle = ActionBarDrawerToggle(this, drawer, toolbar, R.string.app_name, R.string.app_name)

        drawer.addDrawerListener(toggle)
        toggle.syncState()
        */

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
                        tab.text =  "Calendario"
                        tab.setIcon(R.drawable.ic_calendar_24)
                    }
                    3-> {
                        tab.text =  "Hoy"
                        tab.setIcon(R.drawable.ic_today_24)
                    }
                }
            })
        tabLayoutMediator.attach()

    }
}