package com.example.triggerframeworkdemo.broadcast

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.triggerframeworkdemo.service.TestService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            // Start your service here
            val serviceIntent = Intent(context, TestService::class.java)
            context.startService(serviceIntent)
        }
    }
}