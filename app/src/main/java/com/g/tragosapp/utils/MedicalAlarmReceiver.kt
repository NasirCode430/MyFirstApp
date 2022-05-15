package com.g.tragosapp.utils

import android.app.*

import android.content.Intent

import android.os.Build
import android.content.BroadcastReceiver
import android.content.Context

import android.service.notification.NotificationListenerService
import android.util.Log
import androidx.core.app.NotificationCompat
import com.g.tragosapp.R

import com.g.tragosapp.application.MainActivity
import com.g.tragosapp.data.model.Cocktail
import com.g.tragosapp.domain.CocktailRepository

import java.lang.NullPointerException
import java.util.*

class MedicalAlarmReceiver : BroadcastReceiver() {
    var context: Context? = null

    private val message: CharSequence? = null
    private var notifManager: NotificationManager? = null

    var arr: ArrayList<Cocktail>? = null
    override fun onReceive(context: Context, intent: Intent) {
        try {
            this.context = context

            Log.e("DDDTAG", "onReceive: ", )
            createNotification(context)
        } catch (e: NullPointerException) {
            e.printStackTrace()
        }
    }

    private fun createNotification(
        context: Context,
    ) {
        arr = ArrayList()
        val rando = rand()

        val aMessage = "Need some drinks open app now"
        val title = context.getString(R.string.noti_heading) // Default Channel
        var manager: NotificationManager? = null

        val notifyID = 0
        val id = "9876"
        val intent: Intent

        val remainingIntent: PendingIntent
        val builder: NotificationCompat.Builder
        if (manager == null) {
            manager =
                context.getSystemService(NotificationListenerService.NOTIFICATION_SERVICE) as NotificationManager
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_HIGH
            var notificationChannel = manager.getNotificationChannel(id)
            if (notificationChannel == null) {
                notificationChannel = NotificationChannel(id, title, importance)
                notificationChannel.enableVibration(true)
                notificationChannel.vibrationPattern =
                    longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400)
                manager.createNotificationChannel(notificationChannel)
            }
            builder = NotificationCompat.Builder(context, id)

            intent = Intent(context, MainActivity::class.java)

            // Get the PendingIntent containing the entire back stack
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            remainingIntent =
                PendingIntent.getActivity(
                    context,
                    9876,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            builder.setContentTitle(title) // required
                .setSmallIcon(com.g.tragosapp.R.mipmap.ic_launcher) // required
                .setContentText(aMessage) // required
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(remainingIntent)
                .setTicker(aMessage)
                .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
        } else {
            builder = NotificationCompat.Builder(context, id)

            intent = Intent(context, MainActivity::class.java)

            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
            remainingIntent =
                PendingIntent.getActivity(
                    context,
                    9876,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
            builder.setContentTitle(title) // required
                .setSmallIcon(R.mipmap.ic_launcher) // required
                .setContentText(aMessage) // required
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setContentIntent(remainingIntent)
                .setTicker(aMessage)
                .setVibrate(longArrayOf(100, 200, 300, 400, 500, 400, 300, 200, 400))
                .priority = Notification.PRIORITY_HIGH
        }
        val notification = builder.build()
        manager.notify(notifyID, notification)
    }


    fun rand(): Int {
        val r = Random()
        return r.nextInt(8500 - 1) + 1
    }
}