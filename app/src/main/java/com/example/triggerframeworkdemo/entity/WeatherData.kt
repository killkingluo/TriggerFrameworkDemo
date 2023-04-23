package com.example.triggerframeworkdemo.entity

import java.time.LocalDateTime

data class WeatherData(
    val temperature: Double,
    val windSpeed: Double,
    val windDirection: Double,
    val isDay: Int,
    val time: LocalDateTime,
    val weatherType: Int
)