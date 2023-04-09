package com.example.triggerframeworkdemo.workers

import android.content.Context
import android.icu.util.Calendar
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.triggerframeworkdemo.R
import com.example.triggerframeworkdemo.manager.DataManager
import kotlin.random.Random

@Suppress("UNREACHABLE_CODE")
class DailyReminderWorker constructor(
    private val context: Context,
    private val workerParameters: WorkerParameters,
    private val dataManager: DataManager
) : Worker(context, workerParameters) {

    override fun doWork(): Result {
        val a = 4000
        val b =  8000
        val percentage = a.toFloat() / b.toFloat() * 100
        val c1 = inputData.getBoolean("detectWeekendFlag", false)
        if(c1) {
            if(Calendar.getInstance().isWeekend) {
                makeNotification("Today is weekend, let's take a walk~")
                return Result.success()
            }
            else {
                if(percentage < 50) {
                    makeNotification("Today's walk  is ${ String.format("%.2f",percentage) }%, please walk more~")
                    return Result.success()
                }
                else if(percentage < 80) {
                    makeNotification("Today's walk  is ${ String.format("%.2f",percentage) }%, you are doing great~")
                    return Result.success()
                }
                else if(percentage < 100) {
                    makeNotification("Today's walk  is ${ String.format("%.2f",percentage) }%, you are almost there~")
                    return Result.success()
                }
                else {
                    makeNotification("Congratulation! Today's walk  is ${ String.format("%.2f",percentage) }%, you have walked enough~")
                    return Result.success()
                }
            }
        }
        return Result.success()
    }

    private fun makeNotification(text: String) {
        //create a notification
        val notificationBuilder = NotificationCompat.Builder(context, "1")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Testing")
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))

        //show the notification
        NotificationManagerCompat.from(context).notify(Random.nextInt(), notificationBuilder.build())
    }
}