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
import com.kiwipills.kiwipillsapp.NewMedsActivity
import com.kiwipills.kiwipillsapp.R
import com.kiwipills.kiwipillsapp.Utils.Globals
import com.kiwipills.kiwipillsapp.Utils.ImageUtilities
import com.kiwipills.kiwipillsapp.service.Models.Medicament
import com.kiwipills.kiwipillsapp.service.RestEngine
import com.kiwipills.kiwipillsapp.service.Service
import de.hdodenhof.circleimageview.CircleImageView
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class DraftRA(val context: Context, var medicaments:MutableList<Medicament>) : RecyclerView.Adapter<DraftRA.ViewHolder>(),
    Filterable{
    private  val layoutInflater =  LayoutInflater.from(context)

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{

        val txtTitle = itemView?.findViewById<TextView>(R.id.lbl_titleMedicament_draft)
        val txtDescription = itemView?.findViewById<TextView>(R.id.lbl_edscription_draft)
        val txtHourInit = itemView?.findViewById<TextView>(R.id.lbl_hour_draft)
        val imgMedicament = itemView?.findViewById<CircleImageView>(R.id.iv_medicament_draft)
        val txt_medic_id = itemView?.findViewById<TextView>(R.id.txt_medic_id_draft)
        val monday =  itemView?.findViewById<TextView>(R.id.lbl_lun_draft)
        val thuesday =  itemView?.findViewById<TextView>(R.id.lbl_mar_draft)
        val wednesday =  itemView?.findViewById<TextView>(R.id.lbl_mie_draft)
        val thursday =  itemView?.findViewById<TextView>(R.id.lbl_jue_draft)
        val friday =  itemView?.findViewById<TextView>(R.id.lbl_vie_draft)
        val saturday =  itemView?.findViewById<TextView>(R.id.lbl_sab_draft)
        val sunday =  itemView?.findViewById<TextView>(R.id.lbl_dom_draft)
        var deleteBtn = itemView?.findViewById<ImageView>(R.id.btn_delete_draft)
        var editBtn = itemView?.findViewById<ImageView>(R.id.btn_edit_draft)
        var publishBtn = itemView?.findViewById<ImageView>(R.id.btn_publish_draft)

        var medicamentPosition:Int =  0

        init{
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            Log.d("log:", v!!.id.toString())
            when (v!!.id){
                -1->{
                    /*var med_id = v?.findViewById<TextView>(R.id.txt_medic_id_draft)?.text.toString().toInt()
                    delete_medicament(med_id, context)
                    notifyItemRemoved(adapterPosition)*/
                }
            }
        }

    }

    //Elemento a dibujar
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DraftRA.ViewHolder {
        val itemView = this.layoutInflater.inflate(R.layout.item_draft,parent,false)
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

        holder.publishBtn!!.setOnClickListener {
            val positiveButtonClick = { dialog: DialogInterface, which: Int ->
                publishDraft(medicament.id!!, position)
                Toast.makeText(context, "Medicamento " + medicament.name + " publicado", Toast.LENGTH_SHORT).show()
            }

            val negativeButtonClick = { dialog: DialogInterface, which: Int ->
                Toast.makeText(context, "Acción cancelada", Toast.LENGTH_SHORT).show()
            }

            val builder = AlertDialog.Builder(context)
            builder.setTitle("Está por publicar " + medicament.name)
            builder.setMessage("¿Está seguro?")
            builder.setPositiveButton("Si", DialogInterface.OnClickListener(function = positiveButtonClick))
            builder.setNegativeButton("Cancelar", negativeButtonClick)
            builder.show()
        }

        holder.editBtn!!.setOnClickListener {
            val intent = Intent(context, NewMedsActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            intent.putExtra("action", "edit")
            intent.putExtra("med_id", medicament.id)
            context.startActivity(intent)
        }
        holder.deleteBtn!!.setOnClickListener{
            val positiveButtonClick = { dialog: DialogInterface, which: Int ->
                deletedMed(medicament.id!!,position)
                Toast.makeText(context, "Medicamento " + medicament.name + " eliminado", Toast.LENGTH_SHORT).show()
            }

            val negativeButtonClick = { dialog: DialogInterface, which: Int ->
                Toast.makeText(context, "Acción cancelada", Toast.LENGTH_SHORT).show()
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
        TODO("Not yet implemented")
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
    }

    fun publish_draft(med_id: Int, context: Context){

        val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<Int> = service.publishDraft(med_id)

        result.enqueue(object: Callback<Int> {

            override fun onFailure(call: Call<Int>, t: Throwable) {
                Toast.makeText(context,"No se pudo publicar el medicamento", Toast.LENGTH_SHORT).show()
            }
            override fun onResponse(call: Call<Int>, response: Response<Int>) {
                if(response.body() == 1){
                    Toast.makeText(context, "Medicamento publicado", Toast.LENGTH_SHORT).show()
                }else{
                    Toast.makeText(context,"No se pudo publicar medicamento", Toast.LENGTH_SHORT).show()
                }
            }
        })
    }

    fun publishDraft(med_id: Int, medDeletedPos: Int){
        publish_draft(med_id, context)
        notifyItemRemoved(medDeletedPos)
    }
}

