package com.example.triggerframeworkdemo

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class TriggerFrameworkApp: Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //create a notification channel
            val channel = NotificationChannel(
                "1",
                "encourage channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            val notificationManager = getSystemService(NotificationManager::class.java)
            notificationManager.createNotificationChannel(channel)
        }
    }
    companion object {
        lateinit var instance: TriggerFrameworkApp
    }

}