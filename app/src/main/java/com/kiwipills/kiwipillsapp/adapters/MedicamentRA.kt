package com.kiwipills.kiwipillsapp.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kiwipills.kiwipillsapp.R
import com.kiwipills.kiwipillsapp.Utils.ImageUtilities
import com.kiwipills.kiwipillsapp.service.Models.Medicament
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.ArrayList

class MedicamentRA(val context: Context, var medicaments:List<Medicament>) : RecyclerView.Adapter<MedicamentRA.ViewHolder>(),
    Filterable{
    private  val layoutInflater =  LayoutInflater.from(context)
    private val fullAlbums =  ArrayList<Medicament>(medicaments)

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView), View.OnClickListener{

        val txtTitle = itemView?.findViewById<TextView>(R.id.lbl_titleMedicament_i)
        val txtDescription = itemView?.findViewById<TextView>(R.id.lbl_edscription_i)
        val txtHourInit = itemView?.findViewById<TextView>(R.id.lbl_hour_i)
        val imgMedicament = itemView?.findViewById<CircleImageView>(R.id.iv_medicament_i)

        val monday =  itemView?.findViewById<TextView>(R.id.lbl_lun_i)
        val thuesday =  itemView?.findViewById<TextView>(R.id.lbl_mar_i)
        val wednesday =  itemView?.findViewById<TextView>(R.id.lbl_mie_i)
        val thursday =  itemView?.findViewById<TextView>(R.id.lbl_jue_i)
        val friday =  itemView?.findViewById<TextView>(R.id.lbl_vie_i)
        val saturday =  itemView?.findViewById<TextView>(R.id.lbl_sab_i)
        val sunday =  itemView?.findViewById<TextView>(R.id.lbl_dom_i)

        var medicamentPosition:Int =  0

        override fun onClick(p0: View?) {
            TODO("Not yet implemented")
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

        holder.medicamentPosition =  position


    }

    override fun getItemCount(): Int = this.medicaments.size

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }
}

