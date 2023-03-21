package com.example.triggerframeworkdemo.repository

import com.example.triggerframeworkdemo.data_source.CustomWorkDao
import com.example.triggerframeworkdemo.entity.CustomWork
import kotlinx.coroutines.flow.Flow
import java.util.*

class CustomWorkRepository(private val customWorkDao: CustomWorkDao) {
    fun getAllWorks(): Flow<List<CustomWork>> = customWorkDao.getAllWorks()

    fun getWorkById(id: UUID): CustomWork = customWorkDao.getWorkById(id)

    fun getWorkByName(name: String): CustomWork = customWorkDao.getWorkByName(name)

    fun getLastWorkId(): UUID? = customWorkDao.getLastWorkId()

    suspend fun insertWork(customWork: CustomWork) = customWorkDao.insertWork(customWork)

}