package com.example.triggerframeworkdemo.function

import android.icu.util.Calendar
import java.text.SimpleDateFormat
import java.util.*

fun getTodayTimestamp(): Long {
    return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
            Date().time
        )
    )!!.time
}

//calculate the time difference between now and the time to start the worker
fun timeTransform(hour: Int, minute: Int): Long {
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