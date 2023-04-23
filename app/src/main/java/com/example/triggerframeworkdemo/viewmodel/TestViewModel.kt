package com.example.triggerframeworkdemo.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.work.*
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.*
import javax.inject.Inject

interface TestViewModelAbstract {

}

@Suppress("DEPRECATION")
@HiltViewModel
class TestViewModel @Inject constructor(
    application: Application,
) : ViewModel(), TestViewModelAbstract {

    //create a workManager
    val workManager = WorkManager.getInstance(application.applicationContext)

    //calculate the time difference between now and the time to start the worker
}