package com.example.triggerframeworkdemo.workers

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.hilt.work.HiltWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.triggerframeworkdemo.manager.DataManager
import com.example.triggerframeworkdemo.manager.MessageManager
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class WeatherTriggerWorker @AssistedInject constructor(
    @Assisted private val context: Context,
    @Assisted private val workerParameters: WorkerParameters,
    private val dataManager: DataManager,
    private val messageManager: MessageManager
) : Worker(context, workerParameters) {

    private val goodWeather = listOf(0,1,2,3)

    @RequiresApi(Build.VERSION_CODES.O)
    override fun doWork(): Result {
        val weatherState = dataManager.getWeatherState()
        val weatherType = weatherState.currentWeatherData?.weatherType ?: 10
        if (goodWeather.contains(weatherType)) {
            messageManager.messageGenerationAndSend(2, dataManager.getUserType().type,0)
            Log.d("tag", "Success!")
        }
        else {
            Log.d("tag", "Fail! weatherType: $weatherType")
        }
        return Result.success()
    }
}