package com.kiwipills.kiwipillsapp.Utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.SystemClock
import com.kiwipills.kiwipillsapp.service.Models.AlarmReceiver

object AlarmUtils {
    fun setAlarm(i: Int, timestamp: Long?, ctx: Context, msg: String, interval: Int) {
        val alarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val alarmIntent = Intent(ctx, AlarmReceiver::class.java)
        alarmIntent.putExtra("message", msg)
        alarmIntent.data = Uri.parse("custom://" + System.currentTimeMillis())
        val pendingIntent: PendingIntent
        pendingIntent = PendingIntent.getBroadcast(ctx, i, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT)


        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, timestamp!!,
            (1000 * 60 * 2).toLong(), pendingIntent)

        //alarmManager[AlarmManager.RTC_WAKEUP, timestamp!!] = pendingIntent
    }
}