package com.example.greenalert.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ProcessDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
     fun insertProcess(process: ProcessEntity)

    @Query("SELECT * FROM processes ORDER BY processDate DESC")
     fun getAllProcesses(): List<ProcessEntity>
} 