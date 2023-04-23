package com.example.triggerframeworkdemo.manager

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.lifecycle.LiveData
import com.example.triggerframeworkdemo.data_source.TriggerFrameworkAppDatabase
import com.example.triggerframeworkdemo.entity.Record
import com.example.triggerframeworkdemo.entity.UserType
import com.example.triggerframeworkdemo.entity.WeatherState
import com.example.triggerframeworkdemo.function.getTodayTimestamp
import com.example.triggerframeworkdemo.location.LocationTracker
import com.example.triggerframeworkdemo.repository.WeatherRepository
import com.example.triggerframeworkdemo.util.Resource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.lang.Thread.sleep
import java.time.LocalDateTime
import javax.inject.Inject
import kotlin.math.round

class DataManager @Inject constructor(
    private val database: TriggerFrameworkAppDatabase,
    private val weatherRepository: WeatherRepository,
    private val locationTracker: LocationTracker
) {
    private var percentageMessageSendMutableMap = mutableMapOf<Int, Boolean>()
    private var weatherState =
        WeatherState(currentWeatherData = null, isLoading = false, error = null)
    private var userType = UserType(type = 0, updateTime = 0)
    private val ioScope = CoroutineScope(Dispatchers.IO)

    fun getPercentageMessageSendMutableMap(): MutableMap<Int, Boolean> {
        return percentageMessageSendMutableMap
    }

    fun setPercentageMessageSendMutableMap(key: Int, value: Boolean) {
        percentageMessageSendMutableMap[key] = value
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun getWeatherState(): WeatherState {
        if (weatherState.currentWeatherData == null) {
            updateWeatherState()
            sleep(3000)
        } else {
            //if the current weather data is not the latest hour, update it
            if (weatherState.currentWeatherData!!.time.hour != LocalDateTime.now().hour) {
                updateWeatherState()
                sleep(3000)
            }
        }
        return weatherState
    }

    //update weather data
    private fun updateWeatherState() {
        weatherState = weatherState.copy(
            isLoading = true, error = null
        )

        ioScope.launch {
            locationTracker.getCurrentLocation()?.let { location ->
                weatherState = when (val result = weatherRepository.getWeatherData(
                    lat = location.latitude.roundToTwoDecimalPlaces(),
                    long = location.longitude.roundToTwoDecimalPlaces()
                )) {
                    is Resource.Success -> {
                        weatherState.copy(
                            currentWeatherData = result.data, isLoading = false, error = null
                        )
                    }
                    is Resource.Error -> {
                        weatherState.copy(
                            currentWeatherData = null, isLoading = false, error = result.message
                        )
                    }
                }
                Log.d("Location", "lat: ${location.latitude}, long: ${location.longitude}")
            } ?: kotlin.run {
                weatherState = weatherState.copy(
                    currentWeatherData = null, isLoading = false, error = "Location is null"
                )
            }
        }
    }

    //get record as LiveData
    fun getSelectDateRecordAsLiveData(date: Long): LiveData<Record> {
        return database.recordDao.getSelectDateRecordAsLiveData(date)
    }

    //get record
    fun getSelectDateRecord(date: Long): Record? {
        return database.recordDao.getSelectDateRecord(date)
    }

    //for test, insert data to database
    suspend fun insertRecord(record: Record) = database.recordDao.insertRecord(record)

    fun getUserType(): UserType {
        if (userType.updateTime != getTodayTimestamp()) {
            updateUserType()
            return userType
        }
        return userType
    }

    private fun updateUserType() {
        val sevenDayPercentage: MutableList<Int> = mutableListOf()
        var record: Record
        //get the percentage of the last 7 days
        for (i in 0..6) {
            record = getSelectDateRecord(getTodayTimestamp() - i * 86400000) ?: Record(
                getTodayTimestamp() - i * 86400000,
                0,
                0
            )
            sevenDayPercentage.add(
                (record.current_steps.toFloat() / record.target_steps.toFloat() * 100).toInt()
            )
        }
        //Calculate the variance of the last 7 days
        val mean = sevenDayPercentage.sum().toDouble() / sevenDayPercentage.size
        val squaredDifferences = sevenDayPercentage.map { (it - mean) * (it - mean) }
        val variance = squaredDifferences.sum() / (sevenDayPercentage.size - 1)

        //if the variance is greater than 100, the user is a type 1 user
        userType = if (variance < 100 && mean < 30) {
            userType.copy(type = 0, updateTime = getTodayTimestamp())
        } else if (variance > 100 && mean > 50) {
            userType.copy(type = 1, updateTime = getTodayTimestamp())
        } else if (variance < 100 && mean > 50) {
            userType.copy(type = 2, updateTime = getTodayTimestamp())
        } else {
            userType.copy(type = 0, updateTime = getTodayTimestamp())
        }
    }

    private fun Double.roundToTwoDecimalPlaces(): Double {
        return round(this * 100) / 100
    }

}