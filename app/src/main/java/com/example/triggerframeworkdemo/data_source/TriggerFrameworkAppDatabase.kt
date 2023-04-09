package com.example.triggerframeworkdemo.data_source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.triggerframeworkdemo.entity.CustomWork
import com.example.triggerframeworkdemo.entity.Record

@Database(entities = [CustomWork::class, Record::class], version = 1, exportSchema = false)
abstract class TriggerFrameworkAppDatabase: RoomDatabase() {
    abstract val customWorkDao: CustomWorkDao
    abstract val recordDao: RecordDao

    //initial db
    companion object {
        private var INSTANCE: TriggerFrameworkAppDatabase? = null
        fun getInstance(context: Context): TriggerFrameworkAppDatabase{
            if(INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context, TriggerFrameworkAppDatabase::class.java, "triggerFrameworkDemo_db")
                    .fallbackToDestructiveMigration()
                    .build()
            }
            return INSTANCE as TriggerFrameworkAppDatabase
        }
    }
}