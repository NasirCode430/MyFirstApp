package com.g.tragosapp.application

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.PowerManager
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.g.tragosapp.R
import com.g.tragosapp.core.observe
import com.g.tragosapp.databinding.ActivityStartBinding
import com.g.tragosapp.utils.AlarmReceiver
import com.g.tragosapp.utils.MedicalAlarmReceiver
import com.g.tragosapp.utils.showToast
import com.google.android.material.bottomnavigation.BottomNavigationView
import dagger.hilt.android.AndroidEntryPoint
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStartBinding

    @Inject
    lateinit var toastHelper: ToastHelper
    private lateinit var navController: NavController

//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_main)
//
//        // It's very important to set the toolbar to prevent errors of the NPE type,
//        // this is because the application style is .NoActionBar
//        setSupportActionBar(toolbar)
//
//        navController = findNavController(R.id.nav_host_fragment)
//        NavigationUI.setupActionBarWithNavController(this, navController)
//
//        toastHelper.toastMessages.observe(this) {
//            showToast(it)
//        }
//    }
//
//    override fun onSupportNavigateUp(): Boolean {
//        return navController.navigateUp()
//    }


    @SuppressLint("ResourceAsColor")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStartBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        binding.toolbar.setBackgroundColor(R.color.grey)


        setAlarm()

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard
            )
        )
        toastHelper.toastMessages.observe(this) {
            showToast(it)
        }
        // setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    //set Daily Alarm
    private fun setAlarm() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            ignoreBatteryOptimization()
        }

        scheduleAlarm(this)
    }
    @RequiresApi(Build.VERSION_CODES.M)
    private fun ignoreBatteryOptimization() {
        val intent = Intent()
        val packN = packageName
        val pm = getSystemService(Context.POWER_SERVICE) as PowerManager
        if (!pm.isIgnoringBatteryOptimizations(packN)) {
            intent.action = Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS
            intent.data = Uri.parse("package:$packN")
            startActivity(intent)
        }
    }
    fun start(context: Context) {
        val manager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager?
        val dat = Date()
        val cal_alarm: Calendar = Calendar.getInstance()
        val cal_now: Calendar = Calendar.getInstance()
        cal_now.setTime(dat)
        cal_alarm.setTime(dat)
        cal_alarm.set(Calendar.HOUR_OF_DAY, 14)
        cal_alarm.set(Calendar.MINUTE, 0)
        cal_alarm.set(Calendar.SECOND, 0)
        if (cal_alarm.before(cal_now)) {
            cal_alarm.add(Calendar.DATE, 1)
        }
        val myIntent = Intent(context, AlarmReceiver::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context, 0, myIntent, 0)
        manager!![AlarmManager.RTC_WAKEUP, cal_alarm.getTimeInMillis()] = pendingIntent
    }

    fun scheduleAlarm(context: Context) {
        val alarmManager = context.getSystemService(ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, MedicalAlarmReceiver::class.java)
        val bundle = Bundle()
    //   bundle.putSerializable(context.getString(R.string.arg_alarm_obj), MainActivity)
      // intent.putExtra(context.getString(R.string.bundle_alarm_obj), bundle)
        val alarmPendingIntent = PendingIntent.getBroadcast(context, 2345, intent, 0)
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = System.currentTimeMillis()
        calendar[Calendar.HOUR_OF_DAY] = 14
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0

        // if alarm time has already passed, increment day by 1
        if (calendar.timeInMillis <= System.currentTimeMillis()) {
            calendar[Calendar.DAY_OF_MONTH] = calendar[Calendar.DAY_OF_MONTH] + 1
        }
        val RUN_DAILY = (24 * 60 * 60 * 1000).toLong()
        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            RUN_DAILY,
            alarmPendingIntent)


    }


}
