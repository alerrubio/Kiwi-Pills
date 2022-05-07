package com.kiwipills.kiwipillsapp

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.app.TimePickerDialog.OnTimeSetListener
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.Button
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.kiwipills.kiwipillsapp.service.Models.Medicament
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


class NewMedsActivity : AppCompatActivity() {


    lateinit var btn_getStartDate: Button
    lateinit var btn_getStartTime: Button

    var hour: Int = 0
    var minute: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_medicine)

        btn_getStartDate = findViewById(R.id.btn_getStartDate)
        btn_getStartTime = findViewById(R.id.btn_getStartTime)

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

        var actionBar = getSupportActionBar()

        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
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
            val startDate = LocalDate.parse(btn_getStartDate.text.toString(), DateTimeFormatter.ofPattern("dd/MM/yyyy"))
            val startTime = LocalTime.parse(btn_getStartTime.text.toString(), DateTimeFormatter.ofPattern("HH:mm"))

            val duration = txt_duration.text.toString().toInt()
            val hoursInterval = txt_hoursInterval.text.toString().toInt()


            val monday = cb_monday.isChecked
            val thuesday = cb_thuesday.isChecked
            val wednesday = cb_wednesday.isChecked
            val thursday = cb_thursday.isChecked
            val friday = cb_friday.isChecked
            val saturday = cb_saturday.isChecked
            val sunday = cb_sunday.isChecked


            var obj = Medicament(
                0,
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
                sunday
            )

            Log.d("MEDICAMNETO: ", obj.toString())
        }


    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        finish()
        return super.onOptionsItemSelected(item)
    }

    private fun selectDate(){
        var cal = Calendar.getInstance()

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


    fun checkfields(email: String, password: String, username: String) : Boolean {
        if(email == ""){
            Toast.makeText(this@NewMedsActivity, "Correo requerido", Toast.LENGTH_SHORT).show()
            return false
        }
        if(password == ""){
            Toast.makeText(this@NewMedsActivity, "Contraseña requerida", Toast.LENGTH_SHORT).show()
            return false
        }
        if(username == ""){
            Toast.makeText(this@NewMedsActivity, "Usuario requerido", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

}