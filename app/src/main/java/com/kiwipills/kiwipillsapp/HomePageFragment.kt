package com.kiwipills.kiwipillsapp

import android.content.Intent
import android.content.Context
import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.kiwipills.kiwipillsapp.Utils.Globals
import com.kiwipills.kiwipillsapp.adapters.MedicamentCompactRA
import com.kiwipills.kiwipillsapp.service.Models.Medicament
import com.kiwipills.kiwipillsapp.service.RestEngine
import com.kiwipills.kiwipillsapp.service.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

/**
 * A simple [Fragment] subclass.
 */
class HomePageFragment : Fragment() {

    private lateinit var medicamentAdapter: MedicamentCompactRA
    private var allMedicaments = mutableListOf<Medicament>()
    private lateinit var rcListMedicaments : RecyclerView
    private lateinit var contexto: Context
    private lateinit var lunestag: View
    private lateinit var martestag: View
    private lateinit var miercolestag: View
    private lateinit var juevestag: View
    private lateinit var viernestag: View
    private lateinit var sabadotag: View
    private lateinit var domingotag: View
    private lateinit var noMedsItem: View
    private var weekday: Int? = 0
    private var week: Int? = 0
    private var init: Boolean = true

    companion object{
        private  const val ARG_OBJECT = "object"
    }
    //VAMOS A IMPRIMIR CADA UNO:

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_weekly_view, container, false)
    }

    //Va apesar el argumento del adaptador
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Esto es un get si contiene el argumento tal
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            //val textView: TextView = view.findViewById(R.id.lbl_lun_awv)
            //textView.text = "Fragment: " + getInt(ARG_OBJECT).toString()
        }
        val calendar: Calendar = Calendar.getInstance()

        weekday = calendar.get(Calendar.DAY_OF_WEEK)
        week = Calendar.getInstance(TimeZone.getTimeZone("UTC")).get(Calendar.WEEK_OF_YEAR)

        noMedsItem = view.findViewById<LinearLayout>(R.id.no_meds_item_WeeklyView)

        val btnAddMed = view.findViewById<FloatingActionButton>(R.id.btn_addmedmain)
        lunestag = view.findViewById<TextView>(R.id.lbl_lun_awv)
        martestag = view.findViewById<TextView>(R.id.lbl_mar_awv)
        miercolestag = view.findViewById<TextView>(R.id.lbl_mie_awv)
        juevestag = view.findViewById<TextView>(R.id.labl_jue_awv)
        viernestag = view.findViewById<TextView>(R.id.lbl_vie_awv)
        sabadotag = view.findViewById<TextView>(R.id.lbl_sab_awv)
        domingotag = view.findViewById<TextView>(R.id.lbl_dom_awv)


        btnAddMed.setOnClickListener {
            val intent = Intent(activity, NewMedsActivity::class.java)
            startActivity(intent)
        }

        when (weekday){
            1 ->{
                (domingotag as TextView?)?.setTextColor(getResources().getColor(R.color.white))
                domingotag.setBackgroundColor(getResources().getColor(R.color.kiwi_green))
            }
            2->{
                (lunestag as TextView?)?.setTextColor(getResources().getColor(R.color.white))
                lunestag.setBackgroundColor(getResources().getColor(R.color.kiwi_green))
            }
            3->{
                (martestag as TextView?)?.setTextColor(getResources().getColor(R.color.white))
                martestag.setBackgroundColor(getResources().getColor(R.color.kiwi_green))
            }
            4->{
                (miercolestag as TextView?)?.setTextColor(getResources().getColor(R.color.white))
                miercolestag.setBackgroundColor(getResources().getColor(R.color.kiwi_green))
            }
            5->{
                (juevestag as TextView?)?.setTextColor(getResources().getColor(R.color.white))
                juevestag.setBackgroundColor(getResources().getColor(R.color.kiwi_green))
            }
            6->{
                (viernestag as TextView?)?.setTextColor(getResources().getColor(R.color.white))
                viernestag.setBackgroundColor(getResources().getColor(R.color.kiwi_green))
            }
            7->{
                (sabadotag as TextView?)?.setTextColor(getResources().getColor(R.color.white))
                sabadotag.setBackgroundColor(getResources().getColor(R.color.kiwi_green))
            }
        }

        domingotag.setOnClickListener{
            changeWeekDay(1)
        }
        lunestag.setOnClickListener{
            changeWeekDay(2)
        }
        martestag.setOnClickListener{
            changeWeekDay(3)
        }
        miercolestag.setOnClickListener{
            changeWeekDay(4)
        }
        juevestag.setOnClickListener{
            changeWeekDay(5)
        }
        viernestag.setOnClickListener{
            changeWeekDay(6)
        }
        sabadotag.setOnClickListener{
            changeWeekDay(7)
        }

        contexto = view.context
        //RecyclerView
        rcListMedicaments = view.findViewById(R.id.rcListMedicament_awv)
        rcListMedicaments.layoutManager =  LinearLayoutManager(contexto)
        this.medicamentAdapter = MedicamentCompactRA(contexto,allMedicaments)
        rcListMedicaments.adapter = this.medicamentAdapter

        getMedicaments(weekday!!)
    }

    //OBTENER MEDICAMENTOS
    private fun getMedicaments(week_day: Int){
        val user_id = Globals.UserLogged.id!!
        val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<List<Medicament>> = service.getMedicamentsByDay(user_id, week_day)

        result.enqueue(object: Callback<List<Medicament>> {

            override fun onFailure(call: Call<List<Medicament>>, t: Throwable){
                getMedicamentsOffline()
                Toast.makeText(contexto ,"Error al cargar medicamentos", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Medicament>>, response: Response<List<Medicament>>){
                var arrayItems =  response.body()

                if (arrayItems!!.isNotEmpty()){
                    rcListMedicaments.visibility = View.VISIBLE
                    noMedsItem.visibility = View.GONE
                    for (item in arrayItems){
                        allMedicaments.add(item)
                    }
                    rcListMedicaments.adapter = medicamentAdapter
                }else{
                    getMedicamentsOffline()
                }
            }
        })
    }

    private fun getMedicamentsOffline() {
        val arrayItems = Globals.dbHelper.getListOfMedicaments()
        if (arrayItems.isNotEmpty()) {
            noMedsItem.visibility = View.GONE
            for (item in arrayItems) {
                allMedicaments.add(item)
            }
            rcListMedicaments.adapter = medicamentAdapter
        }else{
            noMedsItem.visibility = View.VISIBLE
            rcListMedicaments.visibility = View.INVISIBLE
        }
    }

    fun changeWeekDay(day: Int){
        allMedicaments = mutableListOf<Medicament>()
        deselectDays(day)
        when (day){
            1 ->{
                (domingotag as TextView?)?.setTextColor(resources.getColor(R.color.white))
                domingotag.setBackgroundColor(resources.getColor(R.color.kiwi_green))
            }
            2->{
                (lunestag as TextView?)?.setTextColor(resources.getColor(R.color.white))
                lunestag.setBackgroundColor(resources.getColor(R.color.kiwi_green))
            }
            3->{
                (martestag as TextView?)?.setTextColor(resources.getColor(R.color.white))
                martestag.setBackgroundColor(resources.getColor(R.color.kiwi_green))
            }
            4->{
                (miercolestag as TextView?)?.setTextColor(resources.getColor(R.color.white))
                miercolestag.setBackgroundColor(resources.getColor(R.color.kiwi_green))
            }
            5->{
                (juevestag as TextView?)?.setTextColor(resources.getColor(R.color.white))
                juevestag.setBackgroundColor(resources.getColor(R.color.kiwi_green))
            }
            6->{
                (viernestag as TextView?)?.setTextColor(resources.getColor(R.color.white))
                viernestag.setBackgroundColor(resources.getColor(R.color.kiwi_green))
            }
            7->{
                (sabadotag as TextView?)?.setTextColor(resources.getColor(R.color.white))
                sabadotag.setBackgroundColor(resources.getColor(R.color.kiwi_green))
            }
        }

        getMedicaments(day)
        this.medicamentAdapter = MedicamentCompactRA(contexto,allMedicaments)
    }

    fun deselectDays(activeDay: Int){
        when (activeDay){
            1 ->{
                (lunestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                lunestag.setBackgroundColor(getResources().getColor(R.color.white))
                (martestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                martestag.setBackgroundColor(getResources().getColor(R.color.white))
                (miercolestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                miercolestag.setBackgroundColor(getResources().getColor(R.color.white))
                (juevestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                juevestag.setBackgroundColor(getResources().getColor(R.color.white))
                (viernestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                viernestag.setBackgroundColor(getResources().getColor(R.color.white))
                (sabadotag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                sabadotag.setBackgroundColor(getResources().getColor(R.color.white))
            }
            2->{
                (martestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                martestag.setBackgroundColor(getResources().getColor(R.color.white))
                (miercolestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                miercolestag.setBackgroundColor(getResources().getColor(R.color.white))
                (juevestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                juevestag.setBackgroundColor(getResources().getColor(R.color.white))
                (viernestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                viernestag.setBackgroundColor(getResources().getColor(R.color.white))
                (sabadotag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                sabadotag.setBackgroundColor(getResources().getColor(R.color.white))
                (domingotag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                domingotag.setBackgroundColor(getResources().getColor(R.color.white))
            }
            3->{
                (lunestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                lunestag.setBackgroundColor(getResources().getColor(R.color.white))
                (miercolestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                miercolestag.setBackgroundColor(getResources().getColor(R.color.white))
                (juevestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                juevestag.setBackgroundColor(getResources().getColor(R.color.white))
                (viernestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                viernestag.setBackgroundColor(getResources().getColor(R.color.white))
                (sabadotag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                sabadotag.setBackgroundColor(getResources().getColor(R.color.white))
                (domingotag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                domingotag.setBackgroundColor(getResources().getColor(R.color.white))
            }
            4->{
                (lunestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                lunestag.setBackgroundColor(getResources().getColor(R.color.white))
                (martestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                martestag.setBackgroundColor(getResources().getColor(R.color.white))
                (juevestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                juevestag.setBackgroundColor(getResources().getColor(R.color.white))
                (viernestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                viernestag.setBackgroundColor(getResources().getColor(R.color.white))
                (sabadotag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                sabadotag.setBackgroundColor(getResources().getColor(R.color.white))
                (domingotag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                domingotag.setBackgroundColor(getResources().getColor(R.color.white))
            }
            5->{
                (lunestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                lunestag.setBackgroundColor(getResources().getColor(R.color.white))
                (martestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                martestag.setBackgroundColor(getResources().getColor(R.color.white))
                (miercolestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                miercolestag.setBackgroundColor(getResources().getColor(R.color.white))
                (viernestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                viernestag.setBackgroundColor(getResources().getColor(R.color.white))
                (sabadotag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                sabadotag.setBackgroundColor(getResources().getColor(R.color.white))
                (domingotag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                domingotag.setBackgroundColor(getResources().getColor(R.color.white))
            }
            6->{
                (lunestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                lunestag.setBackgroundColor(getResources().getColor(R.color.white))
                (martestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                martestag.setBackgroundColor(getResources().getColor(R.color.white))
                (miercolestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                miercolestag.setBackgroundColor(getResources().getColor(R.color.white))
                (juevestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                juevestag.setBackgroundColor(getResources().getColor(R.color.white))
                (sabadotag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                sabadotag.setBackgroundColor(getResources().getColor(R.color.white))
                (domingotag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                domingotag.setBackgroundColor(getResources().getColor(R.color.white))
            }
            7->{
                (lunestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                lunestag.setBackgroundColor(getResources().getColor(R.color.white))
                (martestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                martestag.setBackgroundColor(getResources().getColor(R.color.white))
                (miercolestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                miercolestag.setBackgroundColor(getResources().getColor(R.color.white))
                (juevestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                juevestag.setBackgroundColor(getResources().getColor(R.color.white))
                (viernestag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                viernestag.setBackgroundColor(getResources().getColor(R.color.white))
                (domingotag as TextView?)?.setTextColor(getResources().getColor(R.color.black))
                domingotag.setBackgroundColor(getResources().getColor(R.color.white))
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if (init == false){
            changeWeekDay(weekday!!)
        }else {
            init = false
        }
    }

    override fun onPause() {
        super.onPause()
        changeWeekDay(weekday!!)
        Handler().postDelayed({
        }, 1000)
    }
}