package com.example.triggerframeworkdemo.data_source

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.triggerframeworkdemo.entity.Record
import kotlinx.coroutines.flow.Flow

@Dao
interface RecordDao {

    //get all record
    @Query("SELECT * FROM record_table ORDER BY joined_date DESC")
    fun getAllRecords(): Flow<List<Record>>

    //get a selected date Flow<Record>
    @Query("SELECT * FROM record_table WHERE joined_date = :date LIMIT 1")
    fun getSelectDateRecordAsLiveData(date: Long): LiveData<Record>

    //get a selected date Record
    @Query("SELECT * FROM record_table WHERE joined_date = :date LIMIT 1")
    fun getSelectDateRecord(date: Long): Record?


    //get last Record
    @Query("SELECT * FROM record_table ORDER BY joined_date DESC LIMIT 1")
    fun getLastRecord(): Record?

    //check whether data in database
    @Query("SELECT COUNT(*) FROM record_table")
    fun recordCount(): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: Record)

    @Update
    suspend fun updateRecord(record: Record)

    @Delete
    suspend fun deleteRecord(record: Record)

}
