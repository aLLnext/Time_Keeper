package com.timekeeper.Data.Repository

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import com.timekeeper.Data.DAO.ActivityDao
import com.timekeeper.Data.Entity.Activity

class ActivityRepository(private val activityDao: ActivityDao) {
    val allActivities: LiveData<List<Activity>> = activityDao.getAllActivities()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertActivity(activity: Activity) {
        activityDao.insert(activity)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateActivity(activity: Activity) {
        activityDao.update(activity)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun deleteActivity(activity: Activity) {
        activityDao.delete(activity)
    }

}