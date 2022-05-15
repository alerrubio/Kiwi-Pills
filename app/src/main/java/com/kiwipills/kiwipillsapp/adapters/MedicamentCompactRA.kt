
package com.kiwipills.kiwipillsapp.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.kiwipills.kiwipillsapp.R
import com.kiwipills.kiwipillsapp.Utils.Globals
import com.kiwipills.kiwipillsapp.Utils.ImageUtilities
import com.kiwipills.kiwipillsapp.service.Models.Medicament
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*
import kotlin.collections.ArrayList

class MedicamentCompactRA(val context: Context, var medicaments:List<Medicament>) : RecyclerView.Adapter<MedicamentCompactRA.ViewHolder>(),
    Filterable {

    private  val layoutInflater =  LayoutInflater.from(context)
    private val fullAlbums =  ArrayList<Medicament>(medicaments)

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnClickListener{

        val txtTitle = itemView?.findViewById<TextView>(R.id.lbl_title_icm)
        val txtDescription = itemView?.findViewById<TextView>(R.id.lbl_description_icm)
        val txtHourInit = itemView?.findViewById<TextView>(R.id.lbl_hour_icm)
        val imgMedicament = itemView?.findViewById<CircleImageView>(R.id.img_pillbox_item)
        var medicamentPosition:Int =  0

        override fun onClick(p0: View?) {
            TODO("Not yet implemented")
        }

    }

    //Elemento a dibujar
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MedicamentCompactRA.ViewHolder {
        val itemView = this.layoutInflater.inflate(R.layout.item_compact_medicament,parent,false)
        return  ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val medicament =  this.medicaments[position]
        holder.txtTitle!!.text = medicament.name
        holder.txtDescription!!.text = medicament.description
        holder.txtHourInit!!.text = medicament.startTime.toString()

        if(medicament.image != ""){
            var byteArray:ByteArray? = null
            val strImage:String = medicament.image!!.replace("data:image/png;base64,","")
            byteArray =  Base64.getDecoder().decode(strImage)
            holder.imgMedicament!!.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))
        }

        holder.medicamentPosition =  position


    }

    override fun getItemCount(): Int = this.medicaments.size

    override fun getFilter(): Filter {
        TODO("Not yet implemented")
    }

}