package com.example.triggerframeworkdemo.workers

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.triggerframeworkdemo.R
import com.example.triggerframeworkdemo.data_source.TriggerFrameworkAppDatabase
import com.example.triggerframeworkdemo.function.getTodayTimestamp
import com.example.triggerframeworkdemo.manager.DataManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.*
import kotlin.random.Random
@HiltWorker
class TestWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val dataManager: DataManager
) : Worker(context, workerParameters) {

    //private val database = TriggerFrameworkAppDatabase.getInstance(context)

    @SuppressLint("SuspiciousIndentation")
    override fun doWork(): Result {
        //val todayRecord = database.recordDao.getSelectDateRecord(getTodayTimestamp())
        val todayRecord = dataManager.getSelectDateRecord(getTodayTimestamp())
            //makeNotification("Test is progress $i")
            Log.d("tag", "Test is progress ${todayRecord?.target_steps}")
        makeNotification("Test is progress ${todayRecord?.target_steps}")
        return Result.success()
    }

    private fun makeNotification(text: String) {
        //create a notification
        val notificationBuilder = NotificationCompat.Builder(context, "1")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(text)
            .setContentText("Testing")
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))

        //show the notification
        NotificationManagerCompat.from(context).notify(Random.nextInt(), notificationBuilder.build())
    }
}