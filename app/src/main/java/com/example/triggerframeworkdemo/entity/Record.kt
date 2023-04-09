package com.example.triggerframeworkdemo.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "record_table")
data class Record(
    @PrimaryKey
    val joined_date: Long,
    val current_steps: Int,
    val target_steps:Int,
)