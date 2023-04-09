package com.example.triggerframeworkdemo.di

import android.app.Application
import android.content.Context
import com.example.triggerframeworkdemo.data_source.CustomWorkDao
import com.example.triggerframeworkdemo.data_source.RecordDao
import com.example.triggerframeworkdemo.data_source.TriggerFrameworkAppDatabase
import com.example.triggerframeworkdemo.manager.DataManager
import com.example.triggerframeworkdemo.manager.MessageManager
import com.example.triggerframeworkdemo.repository.CustomWorkRepository
import com.example.triggerframeworkdemo.repository.RecordRepository
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
    fun provideRecordRepository(recordDao: RecordDao) : RecordRepository {
        return RecordRepository(recordDao = recordDao)
    }

    @Singleton
    @Provides
    fun provideCustomWorkDao(triggerFrameworkAppDatabase: TriggerFrameworkAppDatabase) : CustomWorkDao {
        return triggerFrameworkAppDatabase.customWorkDao
    }

    @Singleton
    @Provides
    fun provideRecordDao(triggerFrameworkAppDatabase: TriggerFrameworkAppDatabase) : RecordDao {
        return triggerFrameworkAppDatabase.recordDao
    }

    @Singleton
    @Provides
    fun provideDataManager(triggerFrameworkAppDatabase: TriggerFrameworkAppDatabase) : DataManager {
        return DataManager(database = triggerFrameworkAppDatabase)
    }

//    @Singleton
//    @Provides
//    fun provideMessageManager(context: Context) : MessageManager {
//        return MessageManager(context = context)
//    }
}