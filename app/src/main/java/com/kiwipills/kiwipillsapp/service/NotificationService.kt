package com.kiwipills.kiwipillsapp.service

import android.annotation.TargetApi
import android.app.*
import android.content.Intent
import android.graphics.BitmapFactory
import android.media.RingtoneManager
import android.os.Build
import androidx.core.app.NotificationCompat
import com.kiwipills.kiwipillsapp.MainActivity
import com.kiwipills.kiwipillsapp.R

class NotificationService : IntentService {
    private var notificationManager: NotificationManager? = null
    private var pendingIntent: PendingIntent? = null
    var notification: Notification? = null

    constructor(name: String?) : super(name) {}
    constructor() : super("SERVICE") {}

    @TargetApi(Build.VERSION_CODES.O)
    override fun onHandleIntent(intent2: Intent?) {
        val NOTIFICATION_CHANNEL_ID = applicationContext.getString(R.string.app_name)
        val context = this.applicationContext
        notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        val mIntent = Intent(this, MainActivity::class.java)
        val res = this.resources

        val soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)
        val message = intent2!!.getStringExtra("message")


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val NOTIFY_ID = 0 // ID of notification
            val pendingIntent: PendingIntent
            val builder: NotificationCompat.Builder
            var notifManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            if (notifManager == null) {
                notifManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            }
            val importance = NotificationManager.IMPORTANCE_HIGH
            var mChannel = notifManager.getNotificationChannel(NOTIFICATION_CHANNEL_ID)
            if (mChannel == null) {
                mChannel = NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL_ID,
                    importance
                )
                mChannel.enableVibration(true)
                mChannel.vibrationPattern = longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                notifManager.createNotificationChannel(mChannel)
            }
            builder = NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
            mIntent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            pendingIntent =
                PendingIntent.getActivity(context, 0, mIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            builder.setContentTitle(getString(R.string.app_name))
                .setCategory(Notification.CATEGORY_SERVICE)
                .setSmallIcon(R.drawable.ic_myicon) // required
                .setContentText(message)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_myicon))
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setSound(soundUri)
                .setContentIntent(pendingIntent)
                .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
            val notification = builder.build()
            notifManager.notify(NOTIFY_ID, notification)
            startForeground(1, notification)
        } else {
            pendingIntent =
                PendingIntent.getActivity(context, 1, mIntent, PendingIntent.FLAG_UPDATE_CURRENT)
            notification = NotificationCompat.Builder(this)
                .setContentIntent(pendingIntent)
                .setSmallIcon(R.drawable.ic_myicon)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_myicon))
                .setSound(soundUri)
                .setAutoCancel(true)
                .setContentTitle(getString(R.string.app_name))
                .setCategory(Notification.CATEGORY_SERVICE)
                .setContentText(message).build()
            notificationManager!!.notify(NOTIFICATION_ID, notification)
        }
    }

    companion object {
        private const val NOTIFICATION_ID = 1
        private lateinit var msg_txt:String
    }
}