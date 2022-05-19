package com.kiwipills.kiwipillsapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kiwipills.kiwipillsapp.Utils.Globals
import com.kiwipills.kiwipillsapp.adapters.MedicamentCompactRA
import com.kiwipills.kiwipillsapp.adapters.MedicamentPillboxRA
import com.kiwipills.kiwipillsapp.service.Models.Medicament
import com.kiwipills.kiwipillsapp.service.RestEngine
import com.kiwipills.kiwipillsapp.service.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalTime
import java.util.*

class PillboxFragment : Fragment() {

    private lateinit var contexto: Context
    private lateinit var medicamentAdapter: MedicamentPillboxRA
    private lateinit var dialogMedAdapter: MedicamentCompactRA
    private var morning = mutableListOf<Medicament>()
    private var noon = mutableListOf<Medicament>()
    private var evening = mutableListOf<Medicament>()
    private var night = mutableListOf<Medicament>()
    private lateinit var rcMorning : RecyclerView
    private lateinit var rcAfternoon : RecyclerView
    private lateinit var rcEvening : RecyclerView
    private lateinit var rcNight : RecyclerView

    private var weekday: Int? = 0
    private var week: Int? = 0
    private lateinit var morningpillbox: View
    private lateinit var noonpillbox: View
    private lateinit var eveningpillbox: View
    private lateinit var nightpillbox: View

    companion object{
        private  const val ARG_OBJECT = "object"
    }
    //VAMOS A IMPRIMIR CADA UNO:

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_pillbox, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
        }

        contexto = view.context

        val calendar: Calendar = Calendar.getInstance()
        weekday = calendar.get(Calendar.DAY_OF_WEEK)
        //week = Calendar.getInstance(TimeZone.getTimeZone("UTC")).get(Calendar.WEEK_OF_YEAR)

        morningpillbox = view.findViewById<LinearLayout>(R.id.container_morning)
        noonpillbox = view.findViewById<LinearLayout>(R.id.container_day)
        eveningpillbox = view.findViewById<LinearLayout>(R.id.container_evening)
        nightpillbox = view.findViewById<LinearLayout>(R.id.container_night)

        //RecyclerView morning
        rcMorning = view.findViewById(R.id.rv_morning)
        rcMorning.layoutManager =  LinearLayoutManager(contexto)
        //RecyclerView morning
        rcAfternoon = view.findViewById(R.id.rv_noon)
        rcAfternoon.layoutManager =  LinearLayoutManager(contexto)
        //RecyclerView morning
        rcEvening = view.findViewById(R.id.rv_evening)
        rcEvening.layoutManager =  LinearLayoutManager(contexto)
        //RecyclerView morning
        rcNight = view.findViewById(R.id.rv_night)
        rcNight.layoutManager =  LinearLayoutManager(contexto)

        //this.medicamentAdapter = MedicamentPillboxRA(contexto,allMedicaments)

        //Consigue lista de medicamentos
        getMedicaments(weekday!!)

        morningpillbox.setOnClickListener {
            //inflate el dialogo con el diseño
            val mDialogView = LayoutInflater.from(contexto).inflate(R.layout.dialog_pillbox, null)
            //Construir la alerta del dialogo
            val mBuilder = AlertDialog.Builder(contexto)
                .setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            val dialogtitle = mDialogView.findViewById<TextView>(R.id.pillDialogTitle)
            val closedialog = mDialogView.findViewById<TextView>(R.id.closedialog)
            dialogtitle.text = "Pastillas de mañana"
            val dialogrv = mDialogView.findViewById<RecyclerView>(R.id.dialogrv)

            dialogrv.layoutManager =  LinearLayoutManager(contexto)
            dialogMedAdapter = MedicamentCompactRA(contexto,morning)
            dialogrv.adapter = dialogMedAdapter

            closedialog.setOnClickListener{
                mAlertDialog.dismiss()
            }
        }

        noonpillbox.setOnClickListener {
            //inflate el dialogo con el diseño
            val mDialogView = LayoutInflater.from(contexto).inflate(R.layout.dialog_pillbox, null)
            //Construir la alerta del dialogo
            val mBuilder = AlertDialog.Builder(contexto)
                .setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            val dialogtitle = mDialogView.findViewById<TextView>(R.id.pillDialogTitle)
            val closedialog = mDialogView.findViewById<TextView>(R.id.closedialog)
            dialogtitle.text = "Pastillas de tarde"
            val dialogrv = mDialogView.findViewById<RecyclerView>(R.id.dialogrv)

            dialogrv.layoutManager =  LinearLayoutManager(contexto)
            dialogMedAdapter = MedicamentCompactRA(contexto,noon)
            dialogrv.adapter = dialogMedAdapter

            closedialog.setOnClickListener{
                mAlertDialog.dismiss()
            }
        }

        eveningpillbox.setOnClickListener {
            //inflate el dialogo con el diseño
            val mDialogView = LayoutInflater.from(contexto).inflate(R.layout.dialog_pillbox, null)
            //Construir la alerta del dialogo
            val mBuilder = AlertDialog.Builder(contexto)
                .setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            val dialogtitle = mDialogView.findViewById<TextView>(R.id.pillDialogTitle)
            val closedialog = mDialogView.findViewById<TextView>(R.id.closedialog)
            dialogtitle.text = "Pastillas de atardecer"
            val dialogrv = mDialogView.findViewById<RecyclerView>(R.id.dialogrv)
            dialogrv.layoutManager =  LinearLayoutManager(contexto)

            dialogMedAdapter = MedicamentCompactRA(contexto,evening)
            dialogrv.adapter = dialogMedAdapter

            closedialog.setOnClickListener{
                mAlertDialog.dismiss()
            }
        }

        nightpillbox.setOnClickListener {
            //inflate el dialogo con el diseño
            val mDialogView = LayoutInflater.from(contexto).inflate(R.layout.dialog_pillbox, null)
            //Construir la alerta del dialogo
            val mBuilder = AlertDialog.Builder(contexto)
                .setView(mDialogView)
            val mAlertDialog = mBuilder.show()
            val dialogtitle = mDialogView.findViewById<TextView>(R.id.pillDialogTitle)
            val closedialog = mDialogView.findViewById<TextView>(R.id.closedialog)
            dialogtitle.text = "Pastillas de noche"
            val dialogrv = mDialogView.findViewById<RecyclerView>(R.id.dialogrv)

            dialogrv.layoutManager =  LinearLayoutManager(contexto)
            dialogMedAdapter = MedicamentCompactRA(contexto,night)
            dialogrv.adapter = dialogMedAdapter

            closedialog.setOnClickListener{
                mAlertDialog.dismiss()
            }
        }

        val btnAddMed = view.findViewById<FloatingActionButton>(R.id.btn_addmedpillbox)
        btnAddMed.setOnClickListener {
            val intent = Intent(activity, NewMedsActivity::class.java)
            startActivity(intent)
        }

    }

    //OBTENER MEDICAMENTOS
    private fun getMedicaments(week_day: Int){
        val user_id = Globals.UserLogged.id!!
        val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<List<Medicament>> = service.getMedicamentsByDay(user_id, week_day)

        result.enqueue(object: Callback<List<Medicament>> {

            override fun onFailure(call: Call<List<Medicament>>, t: Throwable){
                Toast.makeText(contexto ,"Error al cargar medicamentos", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Medicament>>, response: Response<List<Medicament>>){

                var morningPills = mutableListOf<Medicament>()
                var noonPills = mutableListOf<Medicament>()
                var eveningPills = mutableListOf<Medicament>()
                var nightPills = mutableListOf<Medicament>()

                val mor1: LocalTime = LocalTime.parse("04:59:59")
                val mor2: LocalTime = LocalTime.parse("12:00:00")
                val noon1: LocalTime = LocalTime.parse("11:59:59")
                val noon2: LocalTime = LocalTime.parse("17:00:00")
                val eve1: LocalTime = LocalTime.parse("16:59:59")
                val eve2: LocalTime = LocalTime.parse("21:00:00")
                val night1: LocalTime = LocalTime.parse("20:59:59")
                val night2: LocalTime = LocalTime.parse("05:00:00")

                var arrayItems =  response.body()

                if (arrayItems!!.isNotEmpty()){
                    //noMedsItem.visibility = View.GONE
                    for (item in arrayItems){

                        val medtime: LocalTime = LocalTime.parse(item.startTime)

                        val isMorning =
                            medtime.isAfter(mor1) && medtime.isBefore(mor2)
                        val isNoon =
                            medtime.isAfter( noon1 ) && medtime.isBefore( noon2 )
                        val isEvening =
                            medtime.isAfter( eve1 ) && medtime.isBefore( eve2 )
                        val isNight =
                            ((medtime.isAfter(night1) && medtime.isAfter(night2))||(medtime.isBefore(night1) && medtime.isBefore(night2)))

                        if(isMorning){
                            morningPills.add(item)
                        } else if(isNoon){
                            noonPills.add(item)
                        } else if(isEvening){
                            eveningPills.add(item)
                        } else if(isNight){
                            nightPills.add(item)
                        }
                        //allMedicaments.add(item)
                    }
                    medicamentAdapter = MedicamentPillboxRA(contexto,morningPills)
                    rcMorning.adapter = medicamentAdapter

                    medicamentAdapter = MedicamentPillboxRA(contexto,noonPills)
                    rcAfternoon.adapter = medicamentAdapter

                    medicamentAdapter = MedicamentPillboxRA(contexto,eveningPills)
                    rcEvening.adapter = medicamentAdapter

                    medicamentAdapter = MedicamentPillboxRA(contexto,nightPills)
                    rcNight.adapter = medicamentAdapter

                    morning = morningPills
                    noon = noonPills
                    evening = eveningPills
                    night = nightPills

                }
                //Toast.makeText(contexto,"Medicamentos obtenidos", Toast.LENGTH_LONG).show()

            }
        })
    }

    override fun onResume() {
        super.onResume()
        val calendar: Calendar = Calendar.getInstance()
        weekday = calendar.get(Calendar.DAY_OF_WEEK)
        getMedicaments(weekday!!)
    }

}