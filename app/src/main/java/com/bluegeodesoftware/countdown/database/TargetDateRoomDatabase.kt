package com.bluegeodesoftware.countdown.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.bluegeodesoftware.countdown.dao.TargetDateDao
import com.bluegeodesoftware.countdown.entity.TargetDate


@Database(entities = [TargetDate::class], version = 1, exportSchema = true)
abstract class TargetDateRoomDatabase : RoomDatabase() {

    abstract fun targetDateDao(): TargetDateDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: TargetDateRoomDatabase? = null


        fun getDatabase(context: Context): TargetDateRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TargetDateRoomDatabase::class.java,
                    "targetDate_database"
                ).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}