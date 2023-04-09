package com.example.triggerframeworkdemo.workerui

import android.Manifest
import android.app.Application
import android.content.Intent
import android.os.Build
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.work.*
import com.example.triggerframeworkdemo.service.TestService
import com.example.triggerframeworkdemo.viewmodel.TestViewModel

@Composable
fun WorkerControlButton(testViewModel: TestViewModel, application: Application) {
    //lastWorkId
    val lastWorkId = testViewModel.getLastWorkId()?.observeAsState()
    //get the testWorker information
    val workInfos =
        testViewModel.workManager.getWorkInfosForUniqueWorkLiveData("test1").observeAsState().value

    val testInfo = remember(key1 = workInfos, key2 = lastWorkId) {
        mutableStateOf(
            workInfos?.find { it.id == lastWorkId?.value }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PermissionRequestButton(permission = Manifest.permission.POST_NOTIFICATIONS, name = "Notification")
        PermissionRequestButton(permission = Manifest.permission.ACCESS_BACKGROUND_LOCATION, name = "Background Location")
        PermissionRequestButton(permission = Manifest.permission.ACCESS_FINE_LOCATION, name = "Fine Location")
        Text("Test Worker")
        Button(
            onClick = {
                //create WorkerRequest if it is not created
                testViewModel.createPeriodicWorkerRequest(13, 36)
            }
        ) {
            Text(text = "Create Test")
        }
        //start work
        Button(
            onClick = {
                testViewModel.startPeriodicWork()
            },
            enabled = testInfo.value?.state != WorkInfo.State.RUNNING
        ) {
            Text(text = "Start Test")
        }
        //stop work
        Button(
            onClick = {
                if (lastWorkId != null) {
                    lastWorkId.value?.let { testViewModel.cancelWork(it) }
                }
            },
            enabled = testInfo.value?.state == WorkInfo.State.RUNNING || testInfo.value?.state == WorkInfo.State.ENQUEUED || testInfo.value?.state == WorkInfo.State.BLOCKED
        ) {
            Text(text = "Stop Test")
        }
        Spacer(modifier = Modifier.height(8.dp))
        when (testInfo.value?.state) {
            WorkInfo.State.RUNNING -> Text("Testing...")
            WorkInfo.State.SUCCEEDED -> Text("Test succeeded")
            WorkInfo.State.FAILED -> Text("Test failed")
            WorkInfo.State.CANCELLED -> Text("Test cancelled")
            WorkInfo.State.ENQUEUED -> Text("Test work queuing...")
            WorkInfo.State.BLOCKED -> Text("Test work blocked")
            else -> {}
        }

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