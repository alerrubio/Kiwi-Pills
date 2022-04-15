package com.kiwipills.kiwipillsapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

//Extiende de FragmentStateAdapter

class ViewPagerAdapater(fragment: FragmentActivity) : FragmentStateAdapter(fragment)  {

    //Constante a nivel de clase
    companion object{
        private  const val ARG_OBJECT = "object"
    }

    //cuantos fragments va a tener el swipe
    override fun getItemCount(): Int  = 3

    override fun createFragment(position: Int): Fragment {
        //Vamos a crear el fragment

        lateinit var fragment: Fragment

        //Tenemos 2 formas de pasar información a ese fragment
        //Una pasar los datos por medio de un constructor que no es recomendable
        //La segunda usando los arguments, setar argumentos al adaptador que vamos a
        //mandar a cada instancia
        /*fragment.arguments = Bundle().apply {
            // Our object is just an integer :-P
            putInt(ARG_OBJECT, position + 1)
        }
        return fragment*/

        //En caso de que lo fragments sean diferentes
        //usaremos un when
        /*when(position){
            1 -> {return fragment1}
            2 -> {return fragment2}
        }*/


        //En caso de que lo fragments sean diferentes
        //usaremos un when
        when(position){
            0 -> {
                fragment = HomePageFragment()
                fragment.arguments = Bundle().apply {
                    // Our object is just an integer :-P
                    putInt(ARG_OBJECT, position + 1)
                }
            }
            1 -> {
                fragment = ListaMedicamentosFragment()
                fragment.arguments = Bundle().apply {
                    // Our object is just an integer :-P
                    putInt(ARG_OBJECT, position + 1)
                }
            }
            2 -> {
                fragment = TodayFragment()
                fragment.arguments = Bundle().apply {
                    // Our object is just an integer :-P
                    putInt(ARG_OBJECT, position + 1)
                }
            }
        }

        return fragment
    }

}