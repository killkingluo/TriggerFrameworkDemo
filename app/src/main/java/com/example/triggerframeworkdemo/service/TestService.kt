package com.example.triggerframeworkdemo.service


import android.app.Notification
import android.app.Service
import android.content.Intent
import android.icu.util.Calendar
import android.os.IBinder
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.*
import androidx.lifecycle.Observer
import androidx.work.*
import com.example.triggerframeworkdemo.R
import com.example.triggerframeworkdemo.data_source.TriggerFrameworkAppDatabase
import com.example.triggerframeworkdemo.entity.Record
import com.example.triggerframeworkdemo.function.getTodayTimestamp
import com.example.triggerframeworkdemo.manager.DataManager
import com.example.triggerframeworkdemo.manager.MessageManager
import com.example.triggerframeworkdemo.workers.TestWorker
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import kotlin.random.Random

@AndroidEntryPoint
class TestService : Service(), LifecycleOwner {
    //var triggerFrameworkAppDatabase = TriggerFrameworkAppDatabase.getInstance(this)

    @Inject lateinit var dataManager: DataManager

    //var dataManager: DataManager = DataManager(database = triggerFrameworkAppDatabase)

    var messageManager: MessageManager = MessageManager(this)

    private lateinit var lifecycleRegistry: LifecycleRegistry

    private val tag = "TestService"
    private val ioScope = CoroutineScope(Dispatchers.IO)

    //create a workManager
    private val workManager = WorkManager.getInstance(this)
    private val workInfoLiveData: LiveData<MutableList<WorkInfo>> =
        workManager.getWorkInfosForUniqueWorkLiveData("test")

    //the data that will be passed to the worker
    private var data: Data = Data.Builder()
        .putBoolean("detectWeekendFlag", true)
        .build()

    override fun onBind(p0: Intent?): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onCreate()
        startForeground(Random.nextInt(), makeNotification("Start Test Service"))
        lifecycleRegistry = LifecycleRegistry(this)
        lifecycleRegistry.markState(Lifecycle.State.STARTED)
        stepsDataObserverAndTrigger()
        midDayMessageSendTrigger(15, 54)
        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        workManager.cancelUniqueWork("test")
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
                Log.d(tag, "steps data is empty")
            } else {
                val percentage =
                    (data.current_steps.toFloat() / data.target_steps.toFloat() * 100).toInt()
                val sortedKeys: IntArray =
                    dataManager.getPercentageMessageSendMutableMap().keys.sortedDescending()
                        .toIntArray()
                for (i in sortedKeys) {
                    if (percentage >= i && !dataManager.getPercentageMessageSendMutableMap()[i]!!) {
                        messageManager.evaluatePercentageAndSendMessage(percentage)
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
            dataManager.insertRecord(
                Record(
                    joined_date = getTodayTimestamp(),
                    current_steps = 9999,
                    target_steps = 10000,
                )
            )
        }
    }

    private fun midDayMessageSendTrigger(hour: Int, minute: Int) {

        val oneTimeWorkerRequest = OneTimeWorkRequestBuilder<TestWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                    .build()
            )
            .setInitialDelay(timeTransform(hour, minute), TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        val periodWorkRequest = PeriodicWorkRequestBuilder<TestWorker>(1, TimeUnit.DAYS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                    .build()
            )
            .setInitialDelay(timeTransform(hour, minute), TimeUnit.MILLISECONDS)
            .setInputData(data)
            .build()

        //workManager.enqueueUniqueWork("test", ExistingWorkPolicy.REPLACE, oneTimeWorkerRequest)
        workManager.enqueueUniquePeriodicWork("test", ExistingPeriodicWorkPolicy.REPLACE, periodWorkRequest)
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
