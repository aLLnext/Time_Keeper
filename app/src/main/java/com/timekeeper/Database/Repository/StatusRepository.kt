package com.timekeeper.Database.Repository

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread
import com.timekeeper.Database.DAO.StatusDao
import com.timekeeper.Database.Entity.Status

class StatusRepository(private val statusDao: StatusDao) {

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertStat(status: Status){
        statusDao.insert(status)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateStatus(status: Status){
        statusDao.updateStatus(status)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    fun getStatus(status_id: Int): Status{
        return statusDao.getStatus(status_id)
    }
}