package com.bluegeodesoftware.countdown.entity

import android.content.res.Resources
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.bluegeodesoftware.countdown.R
import java.io.Serializable
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.util.*

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

    public fun getLocaleTarget(): LocalDateTime
    {
        return LocalDateTime.ofEpochSecond(this.epoch_time, 0, ZoneOffset.UTC)
    }

    public fun getCountDownString(resources: Resources): String
    {
        val now = LocalDateTime.now()

        val diff = Duration.between(now, this.getLocaleTarget())
        var diffMinutes = (diff.seconds / 60).toInt()
        val diffSeconds = (diff.seconds % 60).toInt()
        var diffHours = (diffMinutes / 60)
        diffMinutes %= 60
        val diffDays = (diffHours / 24)
        diffHours %= 24

        var countDownString = resources.getQuantityString(
            R.plurals.countdown_with_days,
            diffDays,
            diffDays,
            diffHours,
            diffMinutes,
            diffSeconds
        )

        if (diffDays <= 0) {
            countDownString = resources.getString(
                R.string.countdown_without_days,
                diffHours,
                diffMinutes,
                diffSeconds
            )
        }

        return countDownString
    }
}