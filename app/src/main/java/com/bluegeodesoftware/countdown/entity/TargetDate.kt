package com.bluegeodesoftware.countdown.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "target_date")
data class TargetDate(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "epoch_time") val epoch_time: Long
)
{
    constructor(epoch_time: Long) : this(0, epoch_time)
}