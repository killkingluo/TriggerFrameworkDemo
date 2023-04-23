package com.example.triggerframeworkdemo.repository

import com.example.triggerframeworkdemo.entity.WeatherData
import com.example.triggerframeworkdemo.util.Resource

interface WeatherRepository {
    suspend fun getWeatherData(lat: Double, long: Double):Resource<WeatherData>
}