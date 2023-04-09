package com.example.triggerframeworkdemo

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import com.example.triggerframeworkdemo.ui.theme.TriggerFrameworkDemoTheme
import com.example.triggerframeworkdemo.viewmodel.TestViewModel
import com.example.triggerframeworkdemo.workerui.WorkerControlButton
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val testViewModel: TestViewModel by viewModels()
        setContent {
            TriggerFrameworkDemoTheme {
                WorkerControlButton(testViewModel, application)
            }
        }
    }
}
