package com.example.triggerframeworkdemo.di

import android.app.Application
import android.content.Context
import com.example.triggerframeworkdemo.data_source.CustomWorkDao
import com.example.triggerframeworkdemo.data_source.RecordDao
import com.example.triggerframeworkdemo.data_source.TriggerFrameworkAppDatabase
import com.example.triggerframeworkdemo.data_source.remove.WeatherApi
import com.example.triggerframeworkdemo.location.LocationTracker
import com.example.triggerframeworkdemo.manager.DataManager
import com.example.triggerframeworkdemo.manager.MessageManager
import com.example.triggerframeworkdemo.repository.CustomWorkRepository
import com.example.triggerframeworkdemo.repository.RecordRepository
import com.example.triggerframeworkdemo.repository.WeatherRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideTriggerFrameworkAppDatabase(app: Application): TriggerFrameworkAppDatabase {
        return TriggerFrameworkAppDatabase.getInstance(context = app)
    }

    @Singleton
    @Provides
    fun provideCustomWorkRepository(customWorkDao: CustomWorkDao): CustomWorkRepository {
        return CustomWorkRepository(customWorkDao = customWorkDao)
    }

    @Singleton
    @Provides
    fun provideRecordRepository(recordDao: RecordDao): RecordRepository {
        return RecordRepository(recordDao = recordDao)
    }

    @Singleton
    @Provides
    fun provideCustomWorkDao(triggerFrameworkAppDatabase: TriggerFrameworkAppDatabase): CustomWorkDao {
        return triggerFrameworkAppDatabase.customWorkDao
    }

    @Singleton
    @Provides
    fun provideRecordDao(triggerFrameworkAppDatabase: TriggerFrameworkAppDatabase): RecordDao {
        return triggerFrameworkAppDatabase.recordDao
    }

    @Singleton
    @Provides
    fun provideDataManager(
        triggerFrameworkAppDatabase: TriggerFrameworkAppDatabase,
        weatherRepository: WeatherRepository,
        locationTracker: LocationTracker
    ): DataManager {
        return DataManager(
            database = triggerFrameworkAppDatabase,
            weatherRepository = weatherRepository,
            locationTracker = locationTracker
        )
    }

    @Singleton
    @Provides
    fun provideMessageManager(app: Application) : MessageManager {
        return MessageManager(context = app.applicationContext)
    }

    @Singleton
    @Provides
    fun provideWeatherApi(): WeatherApi {
        return Retrofit.Builder()
            .baseUrl("https://api.open-meteo.com/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()
            .create()
    }

    @Singleton
    @Provides
    fun provideFusedLocationProviderClient(app: Application): FusedLocationProviderClient {
        return LocationServices.getFusedLocationProviderClient(app)
    }

}