package com.bluegeodesoftware.countdown.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.bluegeodesoftware.countdown.dao.TargetDateDao
import com.bluegeodesoftware.countdown.entity.TargetDate


@Database(entities = [TargetDate::class], version = 2, exportSchema = true)
abstract class TargetDateRoomDatabase : RoomDatabase() {

    abstract fun targetDateDao(): TargetDateDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: TargetDateRoomDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    """
                CREATE TABLE new_target_date (
                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                    epoch_time INTEGER NOT NULL, 
                    target_name TEXT NOT NULL DEFAULT '',
                    alarm INTEGER NOT NULL DEFAULT 0, 
                    auto_recur INTEGER NOT NULL DEFAULT 0
                )
                """.trimIndent()
                )
                database.execSQL(
                    """
                INSERT INTO new_target_date (id, epoch_time, target_name, alarm, auto_recur)
                SELECT id, epoch_time, '', 0, 0 FROM target_date
                """.trimIndent()
                )
                database.execSQL("DROP TABLE target_date")
                database.execSQL("ALTER TABLE new_target_date RENAME TO target_date")
            }
        }

        fun getDatabase(context: Context): TargetDateRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TargetDateRoomDatabase::class.java,
                    "targetDate_database"
                ).addMigrations(MIGRATION_1_2).build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }
}