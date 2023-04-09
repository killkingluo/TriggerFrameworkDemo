package com.example.triggerframeworkdemo.function

import java.text.SimpleDateFormat
import java.util.*

fun getTodayTimestamp(): Long {
    return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).parse(
        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(
            Date().time))!!.time
}

