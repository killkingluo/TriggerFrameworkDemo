package com.example.triggerframeworkdemo.workers

import android.content.Context
import android.os.SystemClock.sleep
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.triggerframeworkdemo.R
import kotlin.random.Random

class TestWorker constructor(
    private val context: Context,
    private val workerParameters: WorkerParameters
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        for (i in 1 until 20) {
            sleep(3000L)
            makeNotification("Test is progress $i")
        }
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