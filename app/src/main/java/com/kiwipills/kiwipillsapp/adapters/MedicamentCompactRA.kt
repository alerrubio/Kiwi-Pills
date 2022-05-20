
package com.kiwipills.kiwipillsapp.adapters
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.kiwipills.kiwipillsapp.ListaMedicamentosFragment
import com.kiwipills.kiwipillsapp.MainActivity
import com.kiwipills.kiwipillsapp.R
import com.kiwipills.kiwipillsapp.Utils.Globals
import com.kiwipills.kiwipillsapp.Utils.ImageUtilities
import com.kiwipills.kiwipillsapp.service.Models.Medicament
import com.kiwipills.kiwipillsapp.service.RestEngine
import com.kiwipills.kiwipillsapp.service.Service
import de.hdodenhof.circleimageview.CircleImageView
import okhttp3.internal.notifyAll
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList

class MedicamentCompactRA(val context: Context, var medicaments:List<Medicament>) : RecyclerView.Adapter<MedicamentCompactRA.ViewHolder>(),
    Filterable {

    private  val layoutInflater =  LayoutInflater.from(context)

    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView), View.OnClickListener{

        val txtTitle = itemView?.findViewById<TextView>(R.id.lbl_title_icm)
        val txtDescription = itemView?.findViewById<TextView>(R.id.lbl_description_icm)
        val txtHourInit = itemView?.findViewById<TextView>(R.id.lbl_hour_icm)
        val txt_med_id = itemView?.findViewById<TextView>(R.id.txt_med_id)
        val imgMedicament = itemView?.findViewById<CircleImageView>(R.id.img_pillbox_item)
        var medicamentPosition:Int =  0
        var deleteBtn = itemView?.findViewById<ImageView>(R.id.btn_deleteMed)

        init{
            itemView.setOnClickListener(this)
        }
        override fun onClick(v: View?) {
            Log.d("log:", v!!.id.toString())
            when (v!!.id){
                -1->{
                    var med_id = v?.findViewById<TextView>(R.id.txt_med_id)?.text.toString().toInt()
                    delete_medicament(med_id, context)
                    notifyItemRemoved(this.adapterPosition)
                }
            }
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
        holder.txt_med_id!!.text = medicament.id.toString()


        if(medicament.image != "" ){
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

}