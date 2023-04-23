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
import com.example.triggerframeworkdemo.function.getTodayTimestamp
import com.example.triggerframeworkdemo.manager.DataManager
import com.example.triggerframeworkdemo.manager.MessageManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.util.*
import kotlin.random.Random
@HiltWorker
class EverydayReminderWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val dataManager: DataManager,
    private val messageManager: MessageManager
) : Worker(context, workerParameters) {

    //private val database = TriggerFrameworkAppDatabase.getInstance(context)

    @SuppressLint("SuspiciousIndentation")
    override fun doWork(): Result {
        val todayRecord = dataManager.getSelectDateRecord(getTodayTimestamp())
        if (todayRecord != null) {
            val percentage =
                (todayRecord.current_steps.toFloat() / todayRecord.target_steps.toFloat() * 100).toInt()
            messageManager.messageGenerationAndSend(1, dataManager.getUserType().type, percentage)
        }
        else {
            Log.d("tag", "Fail! todayRecord is null")
        }
        return Result.success()
    }

}