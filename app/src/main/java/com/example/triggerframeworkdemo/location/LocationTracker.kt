package com.example.triggerframeworkdemo.location

import android.location.Location

interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
}