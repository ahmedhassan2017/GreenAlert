package com.example.greenalert.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "processes")
data class ProcessEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val processName: String,
    val processDate: Long, // Store as timestamp
    val indicatorName: String
) 