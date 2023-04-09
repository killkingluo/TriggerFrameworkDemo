package com.example.triggerframeworkdemo.repository

import androidx.lifecycle.LiveData
import com.example.triggerframeworkdemo.data_source.RecordDao
import com.example.triggerframeworkdemo.entity.Record
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class RecordRepository @Inject constructor(private val recordDao: RecordDao) {
    //get all record
    fun getAllRecords(): Flow<List<Record>> = recordDao.getAllRecords()

    //get a selected date Flow<Record>
    fun getSelectDateRecordAsLiveData(date: Long): LiveData<Record> = recordDao.getSelectDateRecordAsLiveData(date)

    //get a selected date Record
    fun getSelectDateRecord(date: Long): Record? = recordDao.getSelectDateRecord(date)

    //get last Record
    fun getLastRecord(): Record? = recordDao.getLastRecord()

    //check whether data in database
    fun recordCount(): Int = recordDao.recordCount()

    suspend fun insertRecord(record: Record) = recordDao.insertRecord(record)

    suspend fun updateRecord(record: Record) = recordDao.updateRecord(record)

    suspend fun deleteRecord(record: Record) = recordDao.deleteRecord(record)
}