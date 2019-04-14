package com.timekeeper.Database.Repository

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread
import com.timekeeper.Database.DAO.ActivityDao
import com.timekeeper.Database.DAO.StatusDao
import com.timekeeper.Database.Entity.Activity
import com.timekeeper.Database.Entity.Status

class ActivityRepository(private val activityDao: ActivityDao) {
    val allActivities: LiveData<List<Activity>> = activityDao.getAllActivities()

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun insertActivity(activity: Activity){
        activityDao.insert(activity)
    }

    @Suppress("RedundantSuspendModifier")
    @WorkerThread
    suspend fun updateActivity(activity: Activity){
        activityDao.update(activity)
    }


//    @Suppress("RedundantSuspendModifier")
//    @WorkerThread
//    suspend fun update(activity: Activity){
//        activityDao.update(activity)
//    }

}