package com.kiwipills.kiwipillsapp

import android.app.TimePickerDialog
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.kiwipills.kiwipillsapp.Utils.AlarmUtils
import java.util.*

class AlarmActivity: AppCompatActivity(), View.OnClickListener {

    //AlarmManager
    //https://www.youtube.com/watch?v=REJ3pDLGTmA&list=PLwba3biRB9dPR7Q4s1u-gG709lIaOLueB&index=44&t=5s&ab_channel=WalkiriaApps
    //https://cursokotlin.com/capitulo-27-timepicker-en-kotlin/
    //https://cursokotlin.com/capitulo-26-datepicker-en-kotlin/
    //1) PONER LOS PERMISOS NECESARIOS EN EL MANIFEST
    //2) CREAR LA ALARMA USANDO ALARMMANAGER
    //3) CONFIGURAR EL MANIFEST.XML PARA RECIBIR EVENTOS DE ALARMA
    //4) CREAR LA CLASE ALARMRECEIVER
    //5) CREAR LA CLASE NOTIFICATIONSERVICE

    private var notificationsTime: TextView? = null
    private var alarmID = 1
    private var settings: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alarm)

        settings = getSharedPreferences(getString(R.string.app_name), Context.MODE_PRIVATE)

        val hour: String?
        val minute: String?

        hour = settings!!.getString("hour", "")
        minute = settings!!.getString("minute", "")

        notificationsTime =  findViewById(R.id.notifications_time)

        if (hour!!.length > 0) {
            notificationsTime!!.setText("$hour:$minute")
        }

        val btnGetDatePicker =  findViewById<Button>(R.id.change_notification)
        btnGetDatePicker.setOnClickListener(this)
    }

    override fun onClick(p0: View?) {

        if(p0!!.id == R.id.change_notification){
            val timePicker = TimePickerFragment { hour, minute-> onTimeSelected(hour, minute) }
            timePicker.show(supportFragmentManager, "timePicker")
        }
    }

    private fun onTimeSelected(hour: Int, minute: Int) {
        notificationsTime!!.setText("$hour:$minute")

        val today = Calendar.getInstance()
        today[Calendar.HOUR_OF_DAY] = hour
        today[Calendar.MINUTE] = minute
        today[Calendar.SECOND] = 0

        val edit: SharedPreferences.Editor = settings!!.edit()
        //edit.putString("hour", finalHour)
        //edit.putString("minute", finalMinute)

        //SAVE ALARM TIME TO USE IT IN CASE OF REBOOT
        edit.putInt("alarmID", alarmID) //el id debe ser dinamico
        edit.putLong("alarmTime", today.timeInMillis)
        edit.commit()

        val horas = 8
        val minutos = horas * 60

        AlarmUtils.setAlarm(alarmID, today.timeInMillis, this@AlarmActivity, "Tomate tus pastillas", minutos.toLong())
    }
}