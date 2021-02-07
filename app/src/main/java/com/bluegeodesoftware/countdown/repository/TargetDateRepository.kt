package com.bluegeodesoftware.countdown.repository

import androidx.annotation.WorkerThread
import com.bluegeodesoftware.countdown.dao.TargetDateDao
import com.bluegeodesoftware.countdown.entity.TargetDate
import kotlinx.coroutines.flow.Flow

class TargetDateRepository(private val targetDateDao: TargetDateDao) {

    // Room executes all queries on a separate thread.
    // Observed Flow will notify the observer when the data has changed.
    val allTargetDates: Flow<List<TargetDate>> = targetDateDao.getAllTargetDates()

    // By default Room runs suspend queries off the main thread, therefore, we don't need to
    // implement anything else to ensure we're not doing long running database work
    // off the main thread.
    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insert(date: TargetDate) {
        targetDateDao.insert(date)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteAll() {
        targetDateDao.deleteAll()
    }
}