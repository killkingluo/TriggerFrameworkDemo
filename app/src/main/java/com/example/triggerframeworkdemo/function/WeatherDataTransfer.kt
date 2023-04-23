package com.example.triggerframeworkdemo.function

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.triggerframeworkdemo.data_source.remove.WeatherDataDto
import com.example.triggerframeworkdemo.entity.WeatherData
import com.example.triggerframeworkdemo.entity.WeatherType
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


@RequiresApi(Build.VERSION_CODES.O)
fun weatherDataTransfer(weatherDataDto: WeatherDataDto): WeatherData {
        return WeatherData(
            temperature = weatherDataDto.temperature,
            windSpeed = weatherDataDto.windSpeed,
            windDirection = weatherDataDto.windDirection,
            isDay = weatherDataDto.isDay,
            time = LocalDateTime.parse(weatherDataDto.time, DateTimeFormatter.ISO_DATE_TIME),
            weatherType = weatherDataDto.weatherCode
        )
    }
