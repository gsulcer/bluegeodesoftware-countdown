package com.bluegeodesoftware.countdown.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.bluegeodesoftware.countdown.entity.TargetDate
import kotlinx.coroutines.flow.Flow

@Dao
interface TargetDateDao {

    @Query("SELECT * FROM target_date ORDER BY epoch_time ASC")
    fun getAllTargetDates(): Flow<List<TargetDate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(date: TargetDate)

    @Query("DELETE FROM target_date")
    suspend fun deleteAll()
}