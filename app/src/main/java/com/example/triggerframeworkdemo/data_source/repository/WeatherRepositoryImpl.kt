package com.example.triggerframeworkdemo.data_source.repository

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.triggerframeworkdemo.data_source.remove.WeatherApi
import com.example.triggerframeworkdemo.entity.WeatherData
import com.example.triggerframeworkdemo.function.weatherDataTransfer
import com.example.triggerframeworkdemo.repository.WeatherRepository
import com.example.triggerframeworkdemo.util.Resource
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val api: WeatherApi,
) : WeatherRepository {
    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun getWeatherData(lat: Double, long: Double): Resource<WeatherData> {
        return try {
            Resource.Success(
                data = weatherDataTransfer(api.getWeatherData(lat = lat, long = long).weatherData)
            )
        } catch (e: Exception) {
            e.printStackTrace()
            Resource.Error(e.message ?: "An unknown error occurred")
        }
    }
}