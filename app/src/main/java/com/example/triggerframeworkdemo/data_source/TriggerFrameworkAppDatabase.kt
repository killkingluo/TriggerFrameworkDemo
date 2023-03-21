package com.example.triggerframeworkdemo.data_source

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.triggerframeworkdemo.entity.CustomWork

@Database(entities = [CustomWork::class], version = 1, exportSchema = false)
abstract class TriggerFrameworkAppDatabase: RoomDatabase() {
    abstract val customWorkDao: CustomWorkDao

    //initial db
    companion object {
        private var INSTANCE: TriggerFrameworkAppDatabase? = null
        fun getInstance(context: Context): TriggerFrameworkAppDatabase{
            if(INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context, TriggerFrameworkAppDatabase::class.java, "triggerFrameworkDemo_db")
                    .fallbackToDestructiveMigration()
                    .allowMainThreadQueries()
                    .build()
            }
            return INSTANCE as TriggerFrameworkAppDatabase
        }
    }
}