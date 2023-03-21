package com.example.triggerframeworkdemo.workerui

import android.Manifest
import android.icu.util.Calendar
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.work.*
import com.example.triggerframeworkdemo.viewmodel.TestViewModel

@Composable
fun WorkerControlButton(testViewModel: TestViewModel) {
    //get the testWorker information
    //val workInfos by WorkManager.getInstance(application.applicationContext).getWorkInfosByTagLiveData("1").observeAsState()
    val workInfos by testViewModel.workManager.getWorkInfosForUniqueWorkLiveData("test1").observeAsState()

    val testInfo = remember(key1 = workInfos) {
        mutableStateOf(
        workInfos?.find { it.id == testViewModel.lastId }
        )
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NotificationPermissionCheckButton(permission = Manifest.permission.POST_NOTIFICATIONS)
        Text("Test Worker")
        //start work
        Button(
            onClick = {
                //create WorkerRequest if it is not created
                testViewModel.createPeriodicWorkerRequest(13,36)
                //start the new one
                //testViewModel.startOneTimeWork()
                testViewModel.startPeriodicWork()
            },
            enabled = testInfo.value?.state != WorkInfo.State.RUNNING
        ) {
            Text(text = "Start Test")
        }
        //stop work
        Button(
            onClick = {
                testViewModel.cancelPeriodicWorker()
            },
            enabled = testInfo.value?.state == WorkInfo.State.RUNNING || testInfo.value?.state == WorkInfo.State.ENQUEUED
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
    }
}