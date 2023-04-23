package com.example.triggerframeworkdemo.service


import android.app.Notification
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.work.*
import com.example.triggerframeworkdemo.R
import com.example.triggerframeworkdemo.entity.Record
import com.example.triggerframeworkdemo.function.getTodayTimestamp
import com.example.triggerframeworkdemo.function.timeTransform
import com.example.triggerframeworkdemo.manager.DataManager
import com.example.triggerframeworkdemo.manager.MessageManager
import com.example.triggerframeworkdemo.workers.EverydayReminderWorker
import com.example.triggerframeworkdemo.workers.WeatherTriggerWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class TestService : Service(), LifecycleOwner {

    @Inject lateinit var dataManager: DataManager
    @Inject lateinit var messageManager: MessageManager

    private lateinit var lifecycleRegistry: LifecycleRegistry

    private val ioScope = CoroutineScope(Dispatchers.IO)

    //create a workManager
    private val workManager = WorkManager.getInstance(this)
//    private val workInfoLiveData: LiveData<MutableList<WorkInfo>> = workManager.getWorkInfosForUniqueWorkLiveData("test")

    //the data that will be passed to the worker
    private var data: Data = Data.Builder()
        .putBoolean("detectWeekendFlag", true)
        .build()

    override fun onBind(p0: Intent?): IBinder? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onCreate()
        startForeground(Random.nextInt(), makeNotification("Start Test Service"))
        lifecycleRegistry = LifecycleRegistry(this)
        lifecycleRegistry.markState(Lifecycle.State.STARTED)
        //stepsDataObserverAndTrigger()
        //everyDayMessageSendTrigger(15, 54)
        weatherTrigger(18, 29)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        workManager.cancelUniqueWork("everyDayMessageSendTrigger")
        workManager.cancelUniqueWork("weatherTrigger")
        lifecycleRegistry.markState(Lifecycle.State.DESTROYED)
    }


    //observe the all data
    private fun stepsDataObserverAndTrigger() {
        //set up the percentage you want
        dataManager.setPercentageMessageSendMutableMap(50, false)
        dataManager.setPercentageMessageSendMutableMap(80, false)
        dataManager.setPercentageMessageSendMutableMap(100, false)
        val todayRecord = dataManager.getSelectDateRecordAsLiveData(getTodayTimestamp())
        //set up the steps data observer
        val infoObserver = Observer<Record> { data ->
            if (data == null) {
                Log.d("TestService", "steps data is empty")
            } else {
                val percentage =
                    (data.current_steps.toFloat() / data.target_steps.toFloat() * 100).toInt()
                val sortedKeys: IntArray =
                    dataManager.getPercentageMessageSendMutableMap().keys.sortedDescending()
                        .toIntArray()
                for (i in sortedKeys) {
                    if (percentage >= i && !dataManager.getPercentageMessageSendMutableMap()[i]!!) {
                        messageManager.messageGenerationAndSend(0, dataManager.getUserType().type, percentage)
                        dataManager.setPercentageMessageSendMutableMap(i, true)
                        for (j in sortedKeys) {
                            if (j < i) {
                                dataManager.setPercentageMessageSendMutableMap(j, true)
                            }
                        }
                    }
                }
            }
        }
        //start observing
        todayRecord.observe(this@TestService, infoObserver)
        //insert test data
        ioScope.launch {
            for(i in 0..6) {
                dataManager.insertRecord(
                    Record(
                        joined_date = getTodayTimestamp()-86400000*i,
                        current_steps = 1500-i,
                        target_steps = 10000,
                    )
                )
            }
        }
    }

    private fun everyDayMessageSendTrigger(hour: Int, minute: Int) {

        val periodWorkRequest = PeriodicWorkRequestBuilder<EverydayReminderWorker>(1, TimeUnit.DAYS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                    .build()
            )
            .setInitialDelay(timeTransform(hour, minute), TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        workManager.enqueueUniquePeriodicWork("everyDayMessageSendTrigger", ExistingPeriodicWorkPolicy.REPLACE, periodWorkRequest)
    }

    private fun weatherTrigger(hour: Int, minute: Int) {
        //insert test data
        ioScope.launch {
            for(i in 0..6) {
                dataManager.insertRecord(
                    Record(
                        joined_date = getTodayTimestamp()-86400000*i,
                        current_steps = 1500-i,
                        target_steps = 10000,
                    )
                )
            }
        }
        val periodWorkRequest = PeriodicWorkRequestBuilder<WeatherTriggerWorker>(1, TimeUnit.DAYS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.NOT_ROAMING)
                    .build()
            )
            .setInitialDelay(timeTransform(hour, minute), TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        workManager.enqueueUniquePeriodicWork("weatherTrigger", ExistingPeriodicWorkPolicy.REPLACE, periodWorkRequest)
    }

    private fun makeNotification(text: String): Notification {
        //create a notification
        val notificationBuilder = NotificationCompat.Builder(this, "1")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Testing")
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))
        return notificationBuilder.build()
    }

    override fun getLifecycle(): Lifecycle {
        return lifecycleRegistry
    }
}
