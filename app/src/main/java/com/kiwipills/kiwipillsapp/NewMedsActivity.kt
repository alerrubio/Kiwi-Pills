package com.kiwipills.kiwipillsapp

import android.app.Activity
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.kiwipills.kiwipillsapp.Utils.*
import com.kiwipills.kiwipillsapp.service.Models.Medicament
import com.kiwipills.kiwipillsapp.service.Models.User
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
    private var settings: SharedPreferences? = null

    var hour: Int = 0
    var minute: Int = 0
    var idAlarm: Int = 0
    var EDIT_MODE: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_medicine)

        EDIT_MODE = intent.extras?.getBoolean("EDIT_MODE", false) == true

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
        val header_title = findViewById<Toolbar>(R.id.toolbarnewmed)
        val btn_selectImage = findViewById<Button>(R.id.btn_selectImage_newMed)

        val btn_addMed = findViewById<Button>(R.id.btn_add_newMed)

        val txt_name = findViewById<TextView>(R.id.txt_name_newMed)
        val txt_description = findViewById<TextView>(R.id.txt_description_newMed)
        val txt_duration = findViewById<TextView>(R.id.txt_duration_newMed)
        val txt_hoursInterval = findViewById<TextView>(R.id.txt_hoursInterval_newMed)
        txt_hoursInterval.setText("8:00")
        val cb_monday = findViewById<CheckBox>(R.id.cb_monday_addMed)
        val cb_thuesday = findViewById<CheckBox>(R.id.cb_thuesday_addMed)
        val cb_wednesday = findViewById<CheckBox>(R.id.cb_wednesday_addMed)
        val cb_thursday = findViewById<CheckBox>(R.id.cb_thursday_addMed)
        val cb_friday = findViewById<CheckBox>(R.id.cb_friday_addMed)
        val cb_saturday = findViewById<CheckBox>(R.id.cb_saturday_addMed)
        val cb_sunday = findViewById<CheckBox>(R.id.cb_sunday_addMed)
        val cb_borrador = findViewById<CheckBox>(R.id.chbox_borrador)

        if (EDIT_MODE){
            header_title.title = "Editar medicamento"
            btn_addMed.text = "Editar"
            txt_name.text = Globals.currMedicine.name
            txt_description.text = Globals.currMedicine.description
            txt_duration.text = Globals.currMedicine.duration.toString()
            var hoursInterval = Globals.currMedicine.hoursInterval?.div(60)

            var reminder = Globals.currMedicine.hoursInterval?.rem(60)
            var min: String = ""
            if (reminder == 0)
                min = "00"
            else{
                min = ((hoursInterval!!*100).toInt() - (hoursInterval!!.toInt() * 100)).toString()
            }

            var auxHoursInterval = hoursInterval.toString() + ":" + min
            txt_hoursInterval.text = auxHoursInterval


            /*var auxStartDate = btn_getStartDate.text.toString()
            var day = auxStartDate[0].toInt()
            var month = auxStartDate[1].toInt()
            var year = auxStartDate[2].toInt()
            var startDateStr = day.toString() + "/" + month.toString() + "/" + year.toString()*/
            btn_getStartDate.setText(actualDate)
            btn_getStartTime.setText(Globals.currMedicine.startTime)
            Globals.currMedicine

            if (Globals.currMedicine.monday == true)
                cb_monday.isChecked = true
            if (Globals.currMedicine.thuesday == true)
                cb_thuesday.isChecked = true
            if (Globals.currMedicine.wednesday == true)
                cb_wednesday.isChecked = true
            if (Globals.currMedicine.thursday == true)
                cb_thursday.isChecked = true
            if (Globals.currMedicine.friday == true)
                cb_friday.isChecked = true
            if (Globals.currMedicine.saturday == true)
                cb_saturday.isChecked = true
            if (Globals.currMedicine.sunday == true)
                cb_sunday.isChecked = true
            if (Globals.currMedicine.draft == true)
                cb_borrador.isChecked = true

            if(Globals.currMedicine.image != ""){
                var byteArray:ByteArray? = null
                val strImage:String = Globals.currMedicine.image!!.replace("data:image/png;base64,","")
                byteArray =  Base64.getDecoder().decode(strImage)
                iv_medicine_picNew.setImageBitmap(ImageUtilities.getBitMapFromByteArray(byteArray))
            }
        }
        val toolbar = findViewById<Toolbar>(R.id.toolbarnewmed)
        setSupportActionBar(toolbar)

        val actionBar = getSupportActionBar()

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
        }

        settings = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)

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
            var hoursInterval: Int = 0

            if(auxDuration != ""){
                duration = auxDuration.toInt()
            }

            val monday = cb_monday.isChecked
            val thuesday = cb_thuesday.isChecked
            val wednesday = cb_wednesday.isChecked
            val thursday = cb_thursday.isChecked
            val friday = cb_friday.isChecked
            val saturday = cb_saturday.isChecked
            val sunday = cb_sunday.isChecked
            val borrador = cb_borrador.isChecked

            val days = arrayOf(
                monday, thuesday, wednesday, thursday, friday, saturday, sunday
            )

            if(checkfields(name, description, duration, auxHours, days)){

                if(auxHours != ""){
                    var auxInterval = auxHours.split(":").toTypedArray()
                    var auxHoraEnMin = auxInterval[0].toInt() * 60
                    var auxMin = auxInterval[1].toInt()
                    hoursInterval = auxHoraEnMin + auxMin
                }
                val end_datetime = Calendar.getInstance()
                var auxStartDate = startDate.split("/").toTypedArray()
                var day = auxStartDate[0].toInt()
                var month = auxStartDate[1].toInt()
                var year = auxStartDate[2].toInt()

                end_datetime[Calendar.DATE] = day
                end_datetime[Calendar.YEAR] = year
                end_datetime[Calendar.MONTH] = month - 1

                end_datetime.add(Calendar.DATE, duration)

                //Imagen
                var encodedString:String = ""
                var strEncodeImage:String = ""
                if(imgArray != null){
                    encodedString =  Base64.getEncoder().encodeToString(imgArray)
                    strEncodeImage= "data:image/png;base64," + encodedString
                }else{
                    if (EDIT_MODE)
                        strEncodeImage= "data:image/png;base64," + Globals.currMedicine.image
                }

                val alarmIds = scheduleAlarm(idAlarm, "KiwiPills: Es hora de tomar " + name, startDate, startTime, hoursInterval, days)
                var aux_med_id: Int = 0

                if (EDIT_MODE)
                    aux_med_id = Globals.currMedicine.id!!


                val obj = Medicament(
                    aux_med_id,
                    Globals.UserLogged.id,
                    name,
                    description,
                    startDate,
                    end_datetime.time.toString(),
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
                    strEncodeImage,
                    alarmIds,
                    borrador
                )

                if (EDIT_MODE){
                    editMed(obj)
                    Log.d("Medicamento editado: ", obj.toString())
                }
                else{
                    addMedicament(obj)
                    Log.d("Medicamento agregado: ", obj.toString())
                }

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

                if(response.body() != 0){
                    medicamentData.id = response.body()
                    Globals.dbHelper.insertMedicament(medicamentData)
                    Toast.makeText(this@NewMedsActivity,"Medicamento agregado",Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this@NewMedsActivity,"No se pudo guardar medicamento",Toast.LENGTH_SHORT).show()
                }

            }
        })
    }

    fun editMed(medicamentData: Medicament){

        val service: Service =  RestEngine.getRestEngine().create(Service::class.java)
        val result: Call<Int> = service.editMed(medicamentData)

        result.enqueue(object: Callback<Int> {
            override fun onFailure(call: Call<Int>, t: Throwable) {
                Toast.makeText(this@NewMedsActivity,"No se pudo editar medicamento",Toast.LENGTH_SHORT).show()
            }

            override fun onResponse(call: Call<Int>, response: Response<Int>) {

                if(response.body() == 1){
                    Globals.dbHelper.updateMedicament(medicamentData)
                    Toast.makeText(this@NewMedsActivity, "Medicamento editado", Toast.LENGTH_SHORT).show()
                    finish()
                }else{
                    Toast.makeText(this@NewMedsActivity,"No se pudo editar medicamento",Toast.LENGTH_SHORT).show()
                }

            }
        })
    }

    fun checkfields( name: String, description: String, duarition: Int, interval: String, days: Array<Boolean> ) : Boolean {
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
        var regex = Regex("(\\d{1,2})\\:(\\d{1,2})")
        if (!interval.matches(regex)){
            Toast.makeText(this@NewMedsActivity,"El formato del intervalo de horas no es válido. Ingrese el dato como HH:MM",Toast.LENGTH_LONG).show()
            return false
        }

        return true
    }

    fun scheduleAlarm(alarmId: Int, msg: String, startDate: String, startTime: String, hoursIntervalInMin: Int, days: Array<Boolean>) : String{
        var alarmsIds: String = ""
        // seteo de alarmas
        val start_datetime = Calendar.getInstance()
        var auxStartDate = startDate.split("/").toTypedArray()
        var auxStartTime = startTime.split(":").toTypedArray()

        var day = auxStartDate[0].toInt()
        var month = auxStartDate[1].toInt()
        var year = auxStartDate[2].toInt()
        var hour = auxStartTime[0].toInt()
        var minute = auxStartTime[1].toInt()

        var interval = 1000 * 60 * hoursIntervalInMin

        var mesesImpares = intArrayOf(1,3,5,7,8,10,12)
        var mesesPares = intArrayOf(4,6,9,11)

        for (dayOfWeek in days){
            if (dayOfWeek == true){

                start_datetime[Calendar.YEAR] = year
                start_datetime[Calendar.MONTH] = month - 1 //Calendar.MONTH empieza en 0
                start_datetime[Calendar.DATE] = day
                start_datetime[Calendar.HOUR_OF_DAY] = hour
                start_datetime[Calendar.MINUTE] = minute
                start_datetime[Calendar.SECOND] = 0


                Log.d("Alarma", " Alarma seteada para el " + day + "/" + month + "/" + year + " a las " + hour + ":" + minute)

                if (mesesImpares.contains(month)) {
                    if (day == 31){
                        day = 1
                        if (month == 12)
                            month = 1
                        else
                            month++
                    }
                    else
                        day++

                }else if (mesesPares.contains(month)){
                    if (day == 30){
                        day = 1
                        if (month == 12)
                            month = 1
                        else
                            month++
                    }
                    else
                        day++
                }else{
                    if (day == 28){
                        day = 1
                        if (month == 12)
                            month = 1
                        else
                            month++
                    }
                    else
                        day++
                }
                AlarmUtils.setAlarm(alarmId,
                    start_datetime.timeInMillis,
                    this@NewMedsActivity,
                    msg,
                    interval.toLong()
                )
                alarmsIds += "$idAlarm,"
                idAlarm++
            }else{
                day++
            }
        }



        val edit: SharedPreferences.Editor = settings!!.edit()
        //edit.putString("hour", finalHour)
        //edit.putString("minute", finalMinute)

        //SAVE ALARM TIME TO USE IT IN CASE OF REBOOT
        edit.putInt("alarmID", alarmId) //el id debe ser dinamico
        edit.putLong("alarmTime", start_datetime.timeInMillis)
        edit.putLong("Interval", interval.toLong())
        edit.commit()

        return alarmsIds
    }
}