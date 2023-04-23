package com.example.triggerframeworkdemo

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.PowerManager
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.DelegatingWorkerFactory
import androidx.work.WorkManager
import com.example.triggerframeworkdemo.broadcast.PowerModeReceiver
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class TriggerFrameworkApp : Application() {

    @Inject lateinit var hiltWorkerFactory: HiltWorkerFactory
    private lateinit var powerModeReceiver: PowerModeReceiver
    override fun onCreate() {
        super.onCreate()
        instance = this
        //create a notification channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "1",
                "encourage channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }

        WorkManager.initialize(this, Configuration.Builder()
            .setWorkerFactory(hiltWorkerFactory)
            .build())

        powerModeReceiver = PowerModeReceiver()
        val intentFilter = IntentFilter()
        intentFilter.addAction(PowerManager.ACTION_POWER_SAVE_MODE_CHANGED)
        registerReceiver(powerModeReceiver, intentFilter)
    }

    override fun onTerminate() {
        unregisterReceiver(powerModeReceiver)
        super.onTerminate()
    }

    companion object {
        lateinit var instance: TriggerFrameworkApp
    }

}