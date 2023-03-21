package com.example.triggerframeworkdemo.viewmodel

import android.app.Application
import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.work.*
import com.example.triggerframeworkdemo.entity.CustomWork
import com.example.triggerframeworkdemo.repository.CustomWorkRepository
import com.example.triggerframeworkdemo.workers.DailyReminderWorker
import com.example.triggerframeworkdemo.workers.TestWorker
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject

interface TestViewModelAbstract {
    fun createOneTimeWorkerRequest(hour: Int, minute: Int)

    fun createPeriodicWorkerRequest(hour: Int, minute: Int)

    fun cancelOneTimeWorker()

    fun cancelPeriodicWorker()

    fun insertWork(customWork: CustomWork)
}

@Suppress("DEPRECATION")
@HiltViewModel
class TestViewModel @Inject constructor(
    application: Application,
    private val customWorkRepository: CustomWorkRepository
) : ViewModel(), TestViewModelAbstract {
    private val ioScope = CoroutineScope(Dispatchers.IO)

    //create a workManager
    val workManager = WorkManager.getInstance(application.applicationContext)

    var oneTimeWorkerRequest = OneTimeWorkRequestBuilder<TestWorker>()
        .setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .build()
        )
        .setInitialDelay(timeTransform(12, 0), TimeUnit.MILLISECONDS)
        .build()

    var periodicWorkerRequest = PeriodicWorkRequestBuilder<TestWorker>(1, TimeUnit.DAYS)
        .setConstraints(
            Constraints.Builder()
                .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                .build()
        )
        .setInitialDelay(timeTransform(12, 0), TimeUnit.MILLISECONDS)
        .build()

    //the data that will be passed to the worker
    var data: Data = Data.Builder()
        .putBoolean("detectWeekendFlag", true)
        .build()

    val lastId = customWorkRepository.getLastWorkId()
    //create oneTimeWorkerRequest
    override fun createOneTimeWorkerRequest(hour: Int, minute: Int) {
        val oneTimeWorkerRequest = OneTimeWorkRequestBuilder<DailyReminderWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                    .build()
            )
            .setInitialDelay(timeTransform(hour, minute), TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()
    }

    override fun createPeriodicWorkerRequest(hour: Int, minute: Int) {
        periodicWorkerRequest = PeriodicWorkRequestBuilder<DailyReminderWorker>(1, TimeUnit.DAYS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                    .build()
            )
            .setInitialDelay(timeTransform(hour, minute), TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        insertWork(
            customWork = CustomWork(periodicWorkerRequest.id, "test1", 0)
        )
    }

    fun startOneTimeWork() {
        workManager.enqueueUniqueWork(
            "test",
            ExistingWorkPolicy.KEEP,
            oneTimeWorkerRequest
        )
    }

    fun startPeriodicWork() {
        workManager.enqueueUniquePeriodicWork(
            "test1",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkerRequest
        )
        insertWork(
            customWork = CustomWork(periodicWorkerRequest.id, "test1", 1)
        )
    }

    override fun cancelOneTimeWorker() {
        if (lastId != null) {
            workManager.cancelWorkById(lastId)
        }
    }

    override fun cancelPeriodicWorker() {
        if (lastId != null) {
            workManager.cancelWorkById(lastId)
        }
    }

    override fun insertWork(customWork: CustomWork) {
        ioScope.launch {
            customWorkRepository.insertWork(customWork)
        }
    }
    //calculate the time difference between now and the time to start the worker
    private fun timeTransform(hour: Int, minute: Int): Long {
        val calendar: Calendar = Calendar.getInstance()
        val nowMillis: Long = calendar.timeInMillis

        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        if (calendar.before(Calendar.getInstance())) {
            calendar.add(Calendar.DATE, 1)
        }

        return calendar.timeInMillis - nowMillis
    }
}