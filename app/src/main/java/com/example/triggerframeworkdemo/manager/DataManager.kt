package com.example.triggerframeworkdemo.manager

import androidx.lifecycle.LiveData
import com.example.triggerframeworkdemo.data_source.TriggerFrameworkAppDatabase
import com.example.triggerframeworkdemo.entity.Record


class DataManager (private val database: TriggerFrameworkAppDatabase) {
    private var percentageMessageSendMutableMap = mutableMapOf<Int, Boolean>()
    private var midDayMessageSend = false

    fun getPercentageMessageSendMutableMap(): MutableMap<Int, Boolean> {
        return percentageMessageSendMutableMap
    }

    fun setPercentageMessageSendMutableMap(key: Int, value: Boolean) {
        percentageMessageSendMutableMap[key] = value
    }

    fun getMidDayMessageSend(): Boolean {
        return midDayMessageSend
    }

    fun setMidDayMessageSend(value: Boolean) {
        midDayMessageSend = value
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

}