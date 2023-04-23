package com.example.triggerframeworkdemo.workerui

import android.Manifest
import android.app.Application
import android.content.Intent
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat
import com.example.triggerframeworkdemo.service.TestService
import com.example.triggerframeworkdemo.viewmodel.TestViewModel

@Composable
fun WorkerControlButton(testViewModel: TestViewModel, application: Application) {

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PermissionRequestButton(permission = Manifest.permission.POST_NOTIFICATIONS, name = "Notification")
        PermissionRequestButton(permission = Manifest.permission.ACCESS_BACKGROUND_LOCATION, name = "Background Location")
        PermissionRequestButton(permission = Manifest.permission.ACCESS_FINE_LOCATION, name = "Fine Location")

        Text("Test Service")
        Button(
            onClick = {
                Intent(application.applicationContext, TestService::class.java).apply {
                    ContextCompat.startForegroundService(application.applicationContext, this)
                }
//                Intent(application.applicationContext, TestService::class.java).apply {
//                    application.startService(this)
//                }
            }
        ) {
            Text(text = "Start Service")
        }
        Button(
            onClick = {
                Intent(application.applicationContext, TestService::class.java).apply {
                    application.stopService(this)
                }
            }
        ) {
            Text(text = "Stop Service")
        }
    }
}