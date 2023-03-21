package com.example.triggerframeworkdemo.di

import android.app.Application
import com.example.triggerframeworkdemo.data_source.CustomWorkDao
import com.example.triggerframeworkdemo.data_source.TriggerFrameworkAppDatabase
import com.example.triggerframeworkdemo.repository.CustomWorkRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideTriggerFrameworkAppDatabase(app: Application) : TriggerFrameworkAppDatabase {
        return TriggerFrameworkAppDatabase.getInstance(context = app)
    }

    @Singleton
    @Provides
    fun provideCustomWorkRepository(customWorkDao: CustomWorkDao) : CustomWorkRepository {
        return CustomWorkRepository(customWorkDao = customWorkDao)
    }

    @Singleton
    @Provides
    fun provideCustomWorkDao(triggerFrameworkAppDatabase: TriggerFrameworkAppDatabase) : CustomWorkDao {
        return triggerFrameworkAppDatabase.customWorkDao
    }
}