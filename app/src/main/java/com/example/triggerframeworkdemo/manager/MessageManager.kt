package com.example.triggerframeworkdemo.manager

import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.triggerframeworkdemo.R
import kotlin.random.Random

class MessageManager(private val context: Context) {

    //get the percentage form Service and evaluate condition to decide whether to send message
    fun evaluatePercentageAndSendMessage(percentage: Int) {
        if(percentage >= 100) {
            makeNotification(title = "Today's Walk Progress",text = "Today's walk is $percentage%, you have complete today's goal~")
        }
        else if(percentage >= 80) {
            makeNotification(title = "Today's Walk Progress",text = "Today's walk is $percentage%, you are doing great~")
        }
        else if(percentage >= 50) {
            makeNotification(title = "Today's Walk Progress",text = "Today's walk is $percentage%, please walk more~")
        }
        else {
            makeNotification(title = "Today's Walk Progress",text = "Today's walk is $percentage%, please walk more~")
        }
    }

    private fun makeNotification(title: String,text: String ) {
        //create a notification
        val notificationBuilder = NotificationCompat.Builder(context, "1")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))

        //show the notification
        NotificationManagerCompat.from(context).notify(Random.nextInt(), notificationBuilder.build())
    }

    //check if now is midday
    fun  isPassMidDay(): Boolean {
        val hour = java.util.Calendar.getInstance().get(java.util.Calendar.HOUR_OF_DAY)
        return hour >= 12
    }
}