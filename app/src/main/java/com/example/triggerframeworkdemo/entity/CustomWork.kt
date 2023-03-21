package com.example.triggerframeworkdemo.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "customwork_table")
data class CustomWork(
    @PrimaryKey
    val id: UUID,
    val name: String,
    val state: Int
)