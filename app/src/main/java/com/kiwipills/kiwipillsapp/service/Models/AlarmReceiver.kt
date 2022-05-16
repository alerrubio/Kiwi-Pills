package com.kiwipills.kiwipillsapp.service.Models

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.content.ContextCompat
import com.kiwipills.kiwipillsapp.service.NotificationService

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        val msg = intent!!.getStringExtra("message")
        val service1 = Intent(context, NotificationService::class.java)
        service1.putExtra("message", msg)
        service1.data = Uri.parse("custom://" + System.currentTimeMillis())
        ContextCompat.startForegroundService(context!!, service1)
        Log.d("WALKIRIA", " ALARM RECEIVED!!!")
    }
}