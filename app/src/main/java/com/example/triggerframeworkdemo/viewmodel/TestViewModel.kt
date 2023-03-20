package com.example.triggerframeworkdemo.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.example.triggerframeworkdemo.workers.TestWorker

interface TestViewModelAbstract {
    fun createWorkerRequest()
    fun startWorker()
    fun cancelWorker()
}

@Suppress("DEPRECATION")
class TestViewModel(application: Application) : ViewModel(), TestViewModelAbstract {
    //create a worker
    private var workManager = WorkManager.getInstance(application.applicationContext)
    private var workerRequest: OneTimeWorkRequest = OneTimeWorkRequestBuilder<TestWorker>().addTag("2").build()

    //create WorkerRequest
    override fun createWorkerRequest() {
        workerRequest = OneTimeWorkRequestBuilder<TestWorker>().addTag("1").build()
    }

    fun getWorkerRequest(): OneTimeWorkRequest {
        return workerRequest
    }

    //run a worker
    override fun startWorker() {
//        workManager.beginUniqueWork(
//            "test",
//            ExistingWorkPolicy.KEEP,
//            workerRequest
//        )
//            .enqueue()
        workManager.enqueue(workerRequest)

    }

    override fun cancelWorker() {
        WorkManager.getInstance().cancelAllWorkByTag("1")
    }
}