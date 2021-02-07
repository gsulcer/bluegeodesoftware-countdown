package com.bluegeodesoftware.countdown

import android.app.Application
import com.bluegeodesoftware.countdown.database.TargetDateRoomDatabase
import com.bluegeodesoftware.countdown.repository.TargetDateRepository

class CountdownApplication : Application() {
    // Using by lazy so the database and the repository are only created when they're needed
    // rather than when the application starts
    private val database by lazy { TargetDateRoomDatabase.getDatabase(this) }
    val repository by lazy { TargetDateRepository(database.targetDateDao()) }
}