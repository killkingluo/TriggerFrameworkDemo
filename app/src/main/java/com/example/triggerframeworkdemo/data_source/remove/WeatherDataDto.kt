package com.example.triggerframeworkdemo.data_source.remove

import com.squareup.moshi.Json

data class WeatherDataDto(
    @field:Json(name = "temperature")
    val temperature: Double,
    @field:Json(name = "windspeed")
    val windSpeed: Double,
    @field:Json(name = "winddirection")
    val windDirection: Double,
    @field:Json(name = "weathercode")
    val weatherCode: Int,
    @field:Json(name = "is_day")
    val isDay: Int,
    @field:Json(name = "time")
    val time: String,
)