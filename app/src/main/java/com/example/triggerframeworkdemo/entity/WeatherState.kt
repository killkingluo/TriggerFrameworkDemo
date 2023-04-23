package com.example.triggerframeworkdemo.entity

data class WeatherState(
    val currentWeatherData: WeatherData? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)