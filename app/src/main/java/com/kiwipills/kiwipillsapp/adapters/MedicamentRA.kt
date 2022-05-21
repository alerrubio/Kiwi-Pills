package com.kiwipills.kiwipillsapp.adapters

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.kiwipills.kiwipillsapp.EditMedActivity
import com.kiwipills.kiwipillsapp.ListaMedicamentosFragment
import com.kiwipills.kiwipillsapp.R
import com.kiwipills.kiwipillsapp.Utils.Globals
import com.kiwipills.kiwipillsapp.Utils.ImageUtilities
import com.kiwipills.kiwipillsapp.service.Models.Medicament
import com.kiwipills.kiwipillsapp.service.RestEngine
import com.kiwipills.kiwipillsapp.service.Service
import de.hdodenhof.circleimageview.CircleImageView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

class MedicamentRA(val context: Context, var medicaments:MutableList<Medicament>) : RecyclerView.Adapter<MedicamentRA.ViewHolder>(),
    Filterable{
    private  val layoutInflater =  LayoutInflater.from(context)
    private val fullMeds =  ArrayList<Medicament>(medicaments)

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{

        val txtTitle = itemView?.findViewById<TextView>(R.id.lbl_titleMedicament_i)
        val txtDescription = itemView?.findViewById<TextView>(R.id.lbl_edscription_i)
        val txtHourInit = itemView?.findViewById<TextView>(R.id.lbl_hour_i)
        val imgMedicament = itemView?.findViewById<CircleImageView>(R.id.iv_medicament_i)
        val txt_medic_id = itemView?.findViewById<TextView>(R.id.txt_medic_id)
        val monday =  itemView?.findViewById<TextView>(R.id.lbl_lun_i)
        val thuesday =  itemView?.findViewById<TextView>(R.id.lbl_mar_i)
        val wednesday =  itemView?.findViewById<TextView>(R.id.lbl_mie_i)
        val thursday =  itemView?.findViewById<TextView>(R.id.lbl_jue_i)
        val friday =  itemView?.findViewById<TextView>(R.id.lbl_vie_i)
        val saturday =  itemView?.findViewById<TextView>(R.id.lbl_sab_i)
        val sunday =  itemView?.findViewById<TextView>(R.id.lbl_dom_i)
        var deleteBtn = itemView?.findViewById<ImageView>(R.id.btn_delete_med)
        var editBtn = itemView?.findViewById<ImageView>(R.id.btn_edit_med)


        var medicamentPosition:Int =  0

        init{
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            Log.d("log:", v!!.id.toString())
            when (v!!.id){
                -1->{
                    /*var med_id = v?.findViewById<TextView>(R.id.txt_medic_id)?.text.toString().toInt()
                    delete_medicament(med_id, context)
                    notifyItemRemoved(adapterPosition)*/
                }
            }
        }

    }

    //Elemento a dibujar
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicamentRA.ViewHolder {
        val itemView = this.layoutInflater.inflate(R.layout.item_medicament,parent,false)
        return  ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val medicament =  this.medicaments[position]
        holder.txtTitle?.text = medicament.name
        holder.txtDescription?.text = medicament.description
        holder.txtHourInit?.text = medicament.startTime.toString()
        holder.txt_medic_id?.text = medicament.id.toString()

        if(medicament.image != ""){
            var byteArray:ByteArray? = null
            val strImage:String = medicament.image!!.replace("data:image/png;base64,","")
            byteArray =  Base64.getDecoder().decode(strImage)
            holder.imgMedicament?.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))
        }

        if(medicament.monday == true){
            holder.monday?.setTextColor(Color.BLACK)
        }
        if(medicament.thuesday == true){
            holder.thuesday?.setTextColor(Color.BLACK)
        }
        if(medicament.wednesday == true){
            holder.wednesday?.setTextColor(Color.BLACK)
        }
        if(medicament.thursday == true){
            holder.thursday?.setTextColor(Color.BLACK)
        }
        if(medicament.friday == true){
            holder.friday?.setTextColor(Color.BLACK)
        }
        if(medicament.saturday == true){
            holder.saturday?.setTextColor(Color.BLACK)
        }
        if(medicament.sunday == true){
            holder.sunday?.setTextColor(Color.BLACK)
        }
        holder.editBtn!!.setOnClickListener {
            val intent = Intent(context, EditMedActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("action", "edit")
            intent.putExtra("id", medicament.id)
            intent.putExtra("user_id", medicament.user_id)
            intent.putExtra("name", medicament.name)
            intent.putExtra("description", medicament.description)
            intent.putExtra("startDate", medicament.startDate)
            intent.putExtra("endDate", medicament.endDate)
            intent.putExtra("startTime", medicament.startTime)
            intent.putExtra("duration", medicament.duration)
            intent.putExtra("hoursInterval", medicament.hoursInterval)
            intent.putExtra("monday", medicament.monday)
            intent.putExtra("thuesday", medicament.thuesday)
            intent.putExtra("wednesday", medicament.wednesday)
            intent.putExtra("thursday", medicament.thursday)
            intent.putExtra("friday", medicament.friday)
            intent.putExtra("saturday", medicament.saturday)
            intent.putExtra("sunday", medicament.sunday)
            intent.putExtra("image", medicament.image)
            intent.putExtra("alarmIds", medicament.alarmIds)
            intent.putExtra("draft", medicament.draft)

            context.startActivity(intent)
        }
        holder.deleteBtn!!.setOnClickListener{
            val positiveButtonClick = { dialog: DialogInterface, which: Int ->
                deletedMed(medicament.id!!,position)
                //Toast.makeText(context, "Medicamento " + medicament.name + " eliminado", Toast.LENGTH_SHORT).show()
            }

            val negativeButtonClick = { dialog: DialogInterface, which: Int ->
                //Toast.makeText(context, "Acción cancelada", Toast.LENGTH_SHORT).show()
            }

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Está por eliminar " + medicament.name)
            builder.setMessage("¿Está seguro?")
            builder.setPositiveButton("Si", DialogInterface.OnClickListener(function = positiveButtonClick))
            builder.setNegativeButton("Cancelar", negativeButtonClick)
            builder.show()
        }
        holder.medicamentPosition =  position

    }

    override fun getItemCount(): Int = this.medicaments.size

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(charSequence: CharSequence?): FilterResults {

                //Obtenemos la cadena
                val filterResults = Filter.FilterResults()
                filterResults.values =  if (charSequence == null || charSequence.isEmpty()){

                    fullMeds as MutableList<Medicament>

                }else{
                    val queryString = charSequence?.toString()?.lowercase()



                    medicaments.filter { med ->

                        med.name!!.toLowerCase().contains(queryString!!)|| med.description!!.toLowerCase().contains(queryString!!)
                    }
                }

                return filterResults
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                medicaments = mutableListOf<Medicament>()
                medicaments =  results?.values as MutableList<Medicament>
                this@MedicamentRA.notifyDataSetChanged()
            }

        }
    }

    fun delete_medicament(med_id: Int, context: Context){

        val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<Int> = service.deleteMedicament(med_id)

        result.enqueue(object: Callback<Int> {

            override fun onFailure(call: Call<Int>, t: Throwable) {
                Toast.makeText(context,"No se pudo eliminar el medicamento", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if(response.body() == 1){
                    Globals.dbHelper.deleteMedicament(med_id)
                    Toast.makeText(context, "Medicamento eliminado", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context,"No se pudo eliminar medicamento", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun deletedMed(med_id: Int, medDeletedPos: Int){
        delete_medicament(med_id, context)
        medicaments.removeAt(medDeletedPos)
        notifyItemRemoved(medDeletedPos)
        getMedicaments()
    }

    private fun getMedicaments(){
        val user_id = Globals.UserLogged.id!!
        val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<List<Medicament>> = service.getMedicaments(user_id)

        result.enqueue(object: Callback<List<Medicament>> {

            override fun onFailure(call: Call<List<Medicament>>, t: Throwable){
                //Toast.makeText(context ,"Error al cargar medicamentos", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<List<Medicament>>, response: Response<List<Medicament>>){
                //var auxList = mutableListOf<Medicament>()
                medicaments = mutableListOf<Medicament>()
                val arrayItems =  response.body()
                if (arrayItems!!.isNotEmpty()){
                    for (item in arrayItems){
                        //auxList.add(item)
                        medicaments.add(item)
                    }
                    this@MedicamentRA.notifyDataSetChanged()
                    //medicaments = auxList
                }
            }
        })
    }
}

