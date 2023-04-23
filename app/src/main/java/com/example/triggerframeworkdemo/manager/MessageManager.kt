package com.example.triggerframeworkdemo.manager

import android.annotation.SuppressLint
import android.content.Context
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.triggerframeworkdemo.R
import kotlin.random.Random

class MessageManager (private val context: Context) {

    val facilitatorMessageList: List<String> = listOf(
        "A good tutorial for walking and jogging: https://www.youtube.com/watch?v=1nBwfZZvjKo",
        "How to exercise through walking? --- https://www.youtube.com/watch?v=1nBwfZZvjKo",
    )

    val sparkMessageList: List<String> = listOf(
        "Walking more is beneficial for health!",
        "Walking is also a form of fitness!"
    )

    val signalMessageList: List<String> = listOf(
        "Please walk more~",
        "You are doing great~",
        "You have complete today's goal~",
        "Good weather for walking~, Let's go out!"
    )

    fun messageGenerationAndSend(triggerType: Int, userType: Int, percentage: Int) {
        //triggerType 0:Percentage, 1:Time, 2:Weather
        //userType 0:Facilitator, 1:Spark, 2:Signal

        var message: String = ""

        when (triggerType) {
            0 -> {
                when (userType) {
                    0 -> {
                        message =
                            "Today's walk is $percentage%," + facilitatorMessageList[Random.nextInt(0, 2)]
                    }
                    1 -> {
                        message = "Today's walk is $percentage%," + sparkMessageList[Random.nextInt(0, 2)]
                    }
                    else -> {
                        message = if (percentage >= 100) {
                            "Today's walk is $percentage%," + signalMessageList[2]
                        } else if (percentage >= 80) {
                            "Today's walk is $percentage%," + signalMessageList[1]
                        } else if (percentage >= 50) {
                            "Today's walk is $percentage%," + signalMessageList[0]
                        } else {
                            "Today's walk is $percentage%," + signalMessageList[0]
                        }
                    }
                }
            }
            1 -> {
                if (percentage < 100) {
                    message = "Today's walk is $percentage%, don't forget today's goal!"
                }
            }
            2 -> {
                message = signalMessageList[3]
            }
        }

        makeNotification(
            title = "Today's Walk Progress",
            text = message
        )
    }

    @SuppressLint("MissingPermission")
    private fun makeNotification(title: String, text: String) {
        //create a notification
        val notificationBuilder = NotificationCompat.Builder(context, "1")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(text)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setVibrate(LongArray(0))

        //show the notification
        NotificationManagerCompat.from(context)
            .notify(Random.nextInt(), notificationBuilder.build())
    }
}