package com.bluegeodesoftware.countdown.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

@Entity(tableName = "target_date")
data class TargetDate(
    @PrimaryKey(autoGenerate = true) val id: Int,
    @ColumnInfo(name = "epoch_time") val epoch_time: Long,
    @ColumnInfo(name = "target_name") val target_name: String,
    @ColumnInfo(name = "alarm") val alarm: Boolean,
    @ColumnInfo(name = "auto_recur") val auto_recur: Boolean
) : Serializable {
    constructor(
        epoch_time: Long,
        target_name: String,
        alarm: Boolean,
        auto_recur: Boolean
    ) : this(0, epoch_time, target_name, alarm, auto_recur)
}