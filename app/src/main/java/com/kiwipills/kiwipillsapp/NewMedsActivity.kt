package com.kiwipills.kiwipillsapp

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.kiwipills.kiwipillsapp.Utils.CAMERA_CODE
import com.kiwipills.kiwipillsapp.Utils.Globals
import com.kiwipills.kiwipillsapp.Utils.IMAGE_PICK_CODE
import com.kiwipills.kiwipillsapp.service.Models.Medicament
import com.kiwipills.kiwipillsapp.service.RestEngine
import com.kiwipills.kiwipillsapp.service.Service
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalQueries.localDate
import java.util.*


class NewMedsActivity : AppCompatActivity() {


    lateinit var iv_medicine_picNew: ImageView
    var imgArray:ByteArray? =  null

    lateinit var btn_getStartDate: Button
    lateinit var btn_getStartTime: Button

    var hour: Int = 0
    var minute: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_medicine)

        btn_getStartDate = findViewById(R.id.btn_getStartDate)
        btn_getStartTime = findViewById(R.id.btn_getStartTime)

        //Obtener fecha y hora actual
        val ad = SimpleDateFormat("dd/MM/yyyy")
        val ah = SimpleDateFormat("hh:mm")
        val actualDate = ad.format(Date()).toString()
        val actualHour = ah.format(Date()).toString()
        btn_getStartDate.setText(actualDate)
        btn_getStartTime.setText(actualHour)


        iv_medicine_picNew = findViewById(R.id.iv_medicine_picNew)
        val btn_selectImage = findViewById<Button>(R.id.btn_selectImage_newMed)

        val btn_addMed = findViewById<Button>(R.id.btn_add_newMed)

        val txt_name = findViewById<TextView>(R.id.txt_name_newMed)
        val txt_description = findViewById<TextView>(R.id.txt_description_newMed)
        val txt_duration = findViewById<TextView>(R.id.txt_duration_newMed)
        val txt_hoursInterval = findViewById<TextView>(R.id.txt_hoursInterval_newMed)
        
        val cb_monday = findViewById<CheckBox>(R.id.cb_monday_addMed)
        val cb_thuesday = findViewById<CheckBox>(R.id.cb_thuesday_addMed)
        val cb_wednesday = findViewById<CheckBox>(R.id.cb_wednesday_addMed)
        val cb_thursday = findViewById<CheckBox>(R.id.cb_thursday_addMed)
        val cb_friday = findViewById<CheckBox>(R.id.cb_friday_addMed)
        val cb_saturday = findViewById<CheckBox>(R.id.cb_saturday_addMed)
        val cb_sunday = findViewById<CheckBox>(R.id.cb_sunday_addMed)

        val toolbar = findViewById<Toolbar>(R.id.toolbarnewmed)
        setSupportActionBar(toolbar)

        val actionBar = getSupportActionBar()

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }


        btn_selectImage.setOnClickListener {
            //inflate el dialogo con el diseño
            val mDialogView = LayoutInflater.from(this).inflate(R.layout.dialog_upload_image, null)
            //Construir la alerta del dialogo
            val mBuilder = AlertDialog.Builder(this)
                .setView(mDialogView)
                .setTitle("Seleccionar")

            val mAlertDialog = mBuilder.show()

            val btnGallery = mDialogView.findViewById<TextView>(R.id.btnGallery)
            val btnCamera = mDialogView.findViewById<TextView>(R.id.btnCamera)

            btnGallery.setOnClickListener {
                mAlertDialog.dismiss()
                openGallery()
            }
            btnCamera.setOnClickListener {
                mAlertDialog.dismiss()
                openCamera()
            }
        }

        btn_getStartDate.setOnClickListener {
            selectDate()
        }

        btn_getStartTime.setOnClickListener {
            selectTime()
        }

        btn_addMed.setOnClickListener {

            val name = txt_name.text.toString()
            val description = txt_description.text.toString()

            //val startDate = LocalDate.parse(btn_getStartDate.text.toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            //val startTime = LocalTime.parse(btn_getStartTime.text.toString(), DateTimeFormatter.ofPattern("HH:mm"))
            val startDate = btn_getStartDate.text.toString()
            val startTime = btn_getStartTime.text.toString()

            val auxDuration = txt_duration.text.toString()
            val auxHours = txt_hoursInterval.text.toString()

            var duration = 0
            var hoursInterval = 0

            if(auxDuration != ""){
                duration = auxDuration.toInt()
            }
            if(auxHours != ""){
                hoursInterval = auxHours.toInt()
            }

            val monday = cb_monday.isChecked
            val thuesday = cb_thuesday.isChecked
            val wednesday = cb_wednesday.isChecked
            val thursday = cb_thursday.isChecked
            val friday = cb_friday.isChecked
            val saturday = cb_saturday.isChecked
            val sunday = cb_sunday.isChecked

            val days = arrayOf(
                monday, thuesday, wednesday, thursday, friday, saturday, sunday
            )

            if(checkfields(name, description, duration, hoursInterval, days)){
                //Imagen
                var encodedString:String = ""
                var strEncodeImage:String = ""
                if(imgArray != null){
                    encodedString =  Base64.getEncoder().encodeToString(imgArray)
                    strEncodeImage= "data:image/png;base64," + encodedString
                }

                val obj = Medicament(
                    0,
                    Globals.UserLogged.id,
                    name,
                    description,
                    startDate,
                    startTime,
                    duration,
                    hoursInterval,
                    monday,
                    thuesday,
                    wednesday,
                    thursday,
                    friday,
                    saturday,
                    sunday,
                    strEncodeImage
                )

                Log.d("Medicamento agregado: : ", obj.toString())

                addMedicament(obj)
            }

        }


    }

    //SELECCIONAR IMAGEN

    fun openGallery(){
        val galleryintent = Intent()
        galleryintent.setType("image/*")
        galleryintent.setAction(Intent.ACTION_GET_CONTENT)
        startActivityForResult(galleryintent, IMAGE_PICK_CODE)
    }
    fun openCamera(){
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, CAMERA_CODE)
    }
    override fun onActivityResult(requestcode: Int, resultcode: Int, data: Intent?) {
        super.onActivityResult(requestcode, resultcode, intent)

        if (resultcode == Activity.RESULT_OK) {

            //RESPUESTA DE LA GALERIA CONTIENE LA IMAGEN
            if (requestcode == IMAGE_PICK_CODE) {

                val uri: Uri? = data?.data
                iv_medicine_picNew.setImageURI(uri)

                val bitmap = (iv_medicine_picNew.drawable as BitmapDrawable).bitmap
                val stream = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream)

                imgArray = stream.toByteArray()

            }
            //RESPUESTA DE LA CÁMARA CONTIENE LA IMAGEN
            if (requestcode == CAMERA_CODE) {

                val uri = data?.extras
                val photo = uri?.get("data") as Bitmap

                val stream = ByteArrayOutputStream()
                //Bitmap.CompressFormat agregar el formato desado, estoy usando aqui jpeg
                photo.compress(Bitmap.CompressFormat.JPEG, 80, stream)
                //Variable donde se guarda la imagen para su envio
                imgArray = stream.toByteArray()

                //Mostramos la imagen en la vista
                iv_medicine_picNew.setImageBitmap(photo)

            }

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    //SELECCIONAR FECHA
    private fun selectDate(){
        val cal = Calendar.getInstance()

        val dpd = DatePickerDialog(this, { view, year, monthOfYear, dayOfMonth ->

            cal.set(Calendar.YEAR, year)
            cal.set(Calendar.MONTH, monthOfYear)
            cal.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.US)
            btn_getStartDate.text = sdf.format(cal.time)

        }, Calendar.YEAR, Calendar.MONTH, Calendar.DAY_OF_MONTH)

        dpd.getDatePicker().setMinDate(Date().time)
        dpd.show()
    }
    //SELECCIONAR HORA
    fun selectTime() {

        val onTimeSetListener =
            OnTimeSetListener { timePicker, selectedHour, selectedMinute ->
                hour = selectedHour
                minute = selectedMinute
                btn_getStartTime.setText(
                    java.lang.String.format(
                        Locale.getDefault(),
                        "%02d:%02d",
                        hour,
                        minute
                    )
                )
            }

        // int style = AlertDialog.THEME_HOLO_DARK;
        val timePickerDialog =
            TimePickerDialog(this,  /*style,*/onTimeSetListener, hour, minute, true)
        timePickerDialog.setTitle("Select Time")
        timePickerDialog.show()
    }

    fun addMedicament(medicamentData: Medicament){

        val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<Int> = service.addMedicament(medicamentData)

        result.enqueue(object: Callback<Int> {
            override fun onFailure(call: Call<Int>, t: Throwable) {
                Toast.makeText(this@NewMedsActivity,"No se pudo guardar medicamento",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Int>, response: Response<Int>) {

                if(response.body() == 1){
                    Toast.makeText(this@NewMedsActivity, "Medicamento guardado", Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this@NewMedsActivity,"No se pudo guardar medicamento",Toast.LENGTH_SHORT).show()
                }

            }
        })
    }

    fun checkfields( name: String, description: String, duarition: Int, interval: Int, days: Array<Boolean>) : Boolean {
        if(name == ""){
            Toast.makeText(this@NewMedsActivity, "Titulo requerido", Toast.LENGTH_SHORT).show()
            return false
        }
        if(description == ""){
            Toast.makeText(this@NewMedsActivity, "Descripcion requerida", Toast.LENGTH_SHORT).show()
            return false
        }

        if(duarition == 0){
            Toast.makeText(this@NewMedsActivity, "Duracion requerida", Toast.LENGTH_SHORT).show()
            return false
        }
        if(interval == 0){
            Toast.makeText(this@NewMedsActivity, "Intervalo de horas requerido", Toast.LENGTH_SHORT).show()
            return false
        }


        var daySelected = false;
        for (day in days) {
            if(day){
                daySelected = true
            }
        }
        if(!daySelected){
            Toast.makeText(this@NewMedsActivity, "Seleccione almenos un dia", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

}