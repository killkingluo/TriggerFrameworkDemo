package com.example.triggerframeworkdemo.data_source.remove

import com.squareup.moshi.Json

data class WeatherDto(
    @field:Json(name = "current_weather")
    val weatherData: WeatherDataDto
)
