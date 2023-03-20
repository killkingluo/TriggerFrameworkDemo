package com.example.triggerframeworkdemo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.example.triggerframeworkdemo.ui.theme.TriggerFrameworkDemoTheme
import com.example.triggerframeworkdemo.viewmodel.TestViewModel
import com.example.triggerframeworkdemo.workerui.WorkerControlButton

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //val testViewModel = TestViewModel(this.application)
        setContent {
            TriggerFrameworkDemoTheme {
                val testViewModel = TestViewModel(application)
                WorkerControlButton(testViewModel, application)
            }
        }
    }
}
