package com.kiwipills.kiwipillsapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.kiwipills.kiwipillsapp.Utils.Globals
import com.kiwipills.kiwipillsapp.adapters.DraftRA
import com.kiwipills.kiwipillsapp.adapters.MedicamentRA
import com.kiwipills.kiwipillsapp.service.Models.Medicament
import com.kiwipills.kiwipillsapp.service.RestEngine
import com.kiwipills.kiwipillsapp.service.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DraftsActivity : AppCompatActivity() {

    private lateinit var medicamentAdapter: DraftRA
    private var allMedicaments = mutableListOf<Medicament>()
    private lateinit var rcListMedicaments : RecyclerView
    private lateinit var contexto: Context
    private lateinit var noMedsItem: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_draft)

        val toolbar = findViewById<Toolbar>(R.id.toolbarDrafts)
        setSupportActionBar(toolbar)

        val actionBar = getSupportActionBar()

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        /*val btnAddMed = findViewById<FloatingActionButton>(R.id.btn_add_myme)
        btnAddMed.setOnClickListener {
            val intent = Intent(this, NewMedsActivity::class.java)
            startActivity(intent)
        }*/

        noMedsItem = findViewById<LinearLayout>(R.id.nomeds_drafts)

        contexto = this@DraftsActivity
        //RecyclerView
        rcListMedicaments = findViewById(R.id.rv_drafts)
        rcListMedicaments.layoutManager =  LinearLayoutManager(this@DraftsActivity)
        medicamentAdapter = DraftRA(this@DraftsActivity, allMedicaments)

        getDrafts()
    }

    //OBTENER MEDICAMENTOS
    private fun getDrafts(){
        val user_id = Globals.UserLogged.id!!
        val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<List<Medicament>> = service.getDrafts(user_id)

        result.enqueue(object: Callback<List<Medicament>> {

            override fun onFailure(call: Call<List<Medicament>>, t: Throwable){
                Toast.makeText(contexto ,t.toString(), Toast.LENGTH_LONG).show()
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
                medicamentAdapter = DraftRA(contexto, allMedicaments)
            }
        })
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }
}