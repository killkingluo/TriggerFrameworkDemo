package com.example.triggerframeworkdemo.data_source

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.triggerframeworkdemo.entity.CustomWork
import kotlinx.coroutines.flow.Flow
import java.util.*

@Dao
interface CustomWorkDao {
    @Query("SELECT * FROM customwork_table ORDER BY id ASC")
    fun getAllWorks(): Flow<List<CustomWork>>

    @Query("SELECT * FROM customwork_table WHERE id = :id")
    fun getWorkById(id: UUID): CustomWork

    @Query("SELECT * FROM customwork_table WHERE name = :name")
    fun getWorkByName(name: String): CustomWork

    @Query("SELECT id FROM customwork_table LIMIT 1")
    fun getLastWorkId(): LiveData<UUID>?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWork(customWork: CustomWork)
}