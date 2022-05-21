package com.kiwipills.kiwipillsapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kiwipills.kiwipillsapp.Utils.Globals
import com.kiwipills.kiwipillsapp.adapters.MedicamentRA
import com.kiwipills.kiwipillsapp.service.Models.Medicament
import com.kiwipills.kiwipillsapp.service.RestEngine
import com.kiwipills.kiwipillsapp.service.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * A simple [Fragment] subclass.
 */
class ListaMedicamentosFragment : Fragment(), SearchView.OnQueryTextListener {

    private lateinit var medicamentAdapter: MedicamentRA
    private var allMedicaments = mutableListOf<Medicament>()
    private lateinit var rcListMedicaments : RecyclerView
    private lateinit var contexto: Context
    private lateinit var noMedsItem: View
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
        return inflater.inflate(R.layout.activity_my_medications, container, false)
    }

    //Va apesar el argumento del adaptador
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Esto es un get si contiene el argumento tal
        arguments?.takeIf { it.containsKey(ARG_OBJECT) }?.apply {
            //val textView: TextView = view.findViewById(R.id.lbl_lun_awv)
            //textView.text = "Fragment: " + getInt(ARG_OBJECT).toString()
        }
        noMedsItem = view.findViewById<LinearLayout>(R.id.no_meds_item_MyMeds)
        val searchmed = view.findViewById<SearchView>(R.id.searchmed)
        val btnAddMed = view.findViewById<FloatingActionButton>(R.id.btn_add_myme)
        btnAddMed.setOnClickListener {
            val intent = Intent(activity, NewMedsActivity::class.java)
            startActivity(intent)
        }
        //rv_medicaments_amm

        contexto = view.context
        //RecyclerView
        rcListMedicaments = view.findViewById(R.id.rv_medicaments_amm)
        rcListMedicaments.layoutManager =  LinearLayoutManager(view.context)
        this.medicamentAdapter = MedicamentRA(view.context, allMedicaments)
        rcListMedicaments.adapter = this.medicamentAdapter
        getMedicaments()

        searchmed.setOnQueryTextListener(this)
    }

    //OBTENER MEDICAMENTOS
    private fun getMedicaments(){
        val user_id = Globals.UserLogged.id!!
        val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<List<Medicament>> = service.getMedicaments(user_id)

        result.enqueue(object: Callback<List<Medicament>> {

            override fun onFailure(call: Call<List<Medicament>>, t: Throwable){
                getMedicamentsOffline()
                //Toast.makeText(contexto ,"Error al cargar medicamentos", Toast.LENGTH_LONG).show()
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
            rcListMedicaments.visibility = View.VISIBLE
            noMedsItem.visibility = View.GONE
            allMedicaments.clear()
            for (item in arrayItems) {
                allMedicaments.add(item)
            }
            rcListMedicaments.adapter = medicamentAdapter
        }else{
            noMedsItem.visibility = View.VISIBLE
            rcListMedicaments.visibility = View.INVISIBLE
        }
    }
    //OBTENER MEDICAMENTOS
    /*private fun getMedicaments(){
        val user_id = Globals.UserLogged.id!!
        val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<List<Medicament>> = service.getMedicaments(user_id)

        result.enqueue(object: Callback<List<Medicament>> {

            override fun onFailure(call: Call<List<Medicament>>, t: Throwable){
                Toast.makeText(contexto ,"Error al cargar medicamentos", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Medicament>>, response: Response<List<Medicament>>){
                val arrayItems =  response.body()
                if (arrayItems!!.isNotEmpty()){
                    noMedsItem.visibility = View.GONE
                    for (item in arrayItems){
                        allMedicaments.add(item)
                    }
                    rcListMedicaments.adapter = medicamentAdapter
                }else{
                    noMedsItem.visibility = View.VISIBLE
                    rcListMedicaments.visibility = View.INVISIBLE
                }
                medicamentAdapter = MedicamentRA(view!!.context, allMedicaments)
                //Toast.makeText(contexto,"Medicamentos obtenidos", Toast.LENGTH_LONG).show()
            }
        })
    }*/

    override fun onResume() {
        super.onResume()
        if (init == false){
            allMedicaments = mutableListOf<Medicament>()
            getMedicaments()
            rcListMedicaments.adapter?.notifyDataSetChanged()
            this.medicamentAdapter = MedicamentRA(requireView().context, allMedicaments)
        }else {
            init = false
        }
    }

    override fun onQueryTextSubmit(p0: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(p0: String?): Boolean {
        if (p0 != null){
            if(medicamentAdapter != null) this.medicamentAdapter?.filter?.filter(p0)
            rcListMedicaments.adapter = medicamentAdapter
            //this.medicamentAdapter = MedicamentRA(contexto, allMedicaments)
            //allMedicaments = mutableListOf<Medicament>()
        }
        return false
    }




}