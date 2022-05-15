package com.g.tragosapp.utils

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import java.util.*

class MedicalWordAlarm {
    companion object {
        var alarmId = 7154
        fun setAlarmTime(ctx: Context) {

                val time = Calendar.getInstance()
                //int a = Integer.parseInt("1010", 8);
                time[Calendar.HOUR] = 2
                time[Calendar.MINUTE] = 0
                time[Calendar.SECOND] = 0
                time[Calendar.MILLISECOND] = 0
                time[Calendar.AM_PM] = Calendar.PM
                if (System.currentTimeMillis() > time.timeInMillis) {
                    val add = AlarmManager.INTERVAL_DAY
                    val oldTime = time.timeInMillis
                    time.timeInMillis = oldTime + add // Okay, then tomorrow ...
                }
                setAlarmEveryDay(ctx, time)

        }

        private fun setAlarmEveryDay(ctx: Context, targetCal: Calendar) {

                val intent = Intent(ctx, MedicalAlarmReceiver::class.java)
                intent.putExtra("ID", alarmId.toString())
                val pendingIntent = PendingIntent.getBroadcast(
                    ctx,
                    alarmId,
                    intent,
                    PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                val alarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.setRepeating(
                    AlarmManager.RTC_WAKEUP,
                    targetCal.timeInMillis,
                    AlarmManager.INTERVAL_DAY,
                    pendingIntent
                )

        }

        fun cancelAlarm(ctx: Context) {

                val intent = Intent(ctx, MedicalAlarmReceiver::class.java)
                intent.putExtra("ID", alarmId.toString())
                val pendingIntent = PendingIntent.getBroadcast(
                    ctx,
                    alarmId,
                    intent,
                    PendingIntent.FLAG_CANCEL_CURRENT or PendingIntent.FLAG_IMMUTABLE
                )
                val alarmManager = ctx.getSystemService(Context.ALARM_SERVICE) as AlarmManager
                alarmManager.cancel(pendingIntent)
                pendingIntent.cancel()

        }
    }
}