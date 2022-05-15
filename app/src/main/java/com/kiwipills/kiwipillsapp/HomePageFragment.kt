package com.kiwipills.kiwipillsapp

import android.content.Intent
import android.content.Context
import android.os.Bundle
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

/**
 * A simple [Fragment] subclass.
 */
class HomePageFragment : Fragment() {

    private lateinit var medicamentAdapter: MedicamentCompactRA
    private val allMedicaments = mutableListOf<Medicament>()
    private lateinit var rcListMedicaments : RecyclerView
    private lateinit var contexto: Context
    private lateinit var noMedsItem: View

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
        noMedsItem = view.findViewById<LinearLayout>(R.id.no_meds_item_WeeklyView)

        val btnAddMed = view.findViewById<FloatingActionButton>(R.id.btn_addmedmain)
        btnAddMed.setOnClickListener {
            val intent = Intent(activity, NewMedsActivity::class.java)
            startActivity(intent)
        }

        /*
        val obj = Medicament(0,0,"Medicamento 1")
        val obj1 = Medicament(0,0,"Medicamento 2")

        val lista = mutableListOf<Medicament>()
        lista.add(obj)
        lista.add(obj1)

        //RecyclerView
        val rcListMedicament = view.findViewById<RecyclerView>(R.id.rcListMedicament_awv)
        rcListMedicament.layoutManager =  LinearLayoutManager(view.context)
        this.medicamentAdapter = MedicamentRecyclerAdapter(view.context, lista)
        rcListMedicament.adapter = this.medicamentAdapter
        */

        contexto = view.context
        //RecyclerView
        rcListMedicaments = view.findViewById(R.id.rcListMedicament_awv)
        rcListMedicaments.layoutManager =  LinearLayoutManager(contexto)
        this.medicamentAdapter = MedicamentCompactRA(contexto,allMedicaments)
        //rcListMedicaments.adapter = this.medicamentAdapter

        getMedicaments()

    }


    //OBTENER MEDICAMENTOS
    private fun getMedicaments(){
        val user_id = Globals.UserLogged.id!!
        val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<List<Medicament>> = service.getMedicaments(user_id)

        result.enqueue(object: Callback<List<Medicament>> {

            override fun onFailure(call: Call<List<Medicament>>, t: Throwable){
                Toast.makeText(contexto ,"Error al cargar medicamentos", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Medicament>>, response: Response<List<Medicament>>){

                var arrayItems =  response.body()

                if (arrayItems!!.isNotEmpty()){
                    noMedsItem.visibility = View.GONE
                    for (item in arrayItems){
                        allMedicaments.add(item)
                    }
                    rcListMedicaments.adapter = medicamentAdapter
                }
                //Toast.makeText(contexto,"Medicamentos obtenidos", Toast.LENGTH_LONG).show()

            }
        })
    }
}