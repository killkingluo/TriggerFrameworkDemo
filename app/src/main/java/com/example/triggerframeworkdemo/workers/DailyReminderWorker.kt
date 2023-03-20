package com.example.triggerframeworkdemo.workers

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import com.example.triggerframeworkdemo.R
import kotlinx.coroutines.delay
import kotlin.random.Random

class DailyReminderWorker constructor(
    private val context: Context,
    private val workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    override suspend fun doWork(): Result {
        for (i in 1 until 20) {
            delay(3000L)
            startForegroundService("Test is progress $i")
        }
        return Result.succ
    }

    private suspend fun startForegroundService(text: String) {
        setForeground(
            ForegroundInfo(
                Random.nextInt(),
                NotificationCompat.Builder(context, "1")
                    .setContentText("Testing")
                    .setContentTitle(text)
                    .setSmallIcon(R.drawable.ic_launcher_background)
                    .build()
            )
        )
    }
}