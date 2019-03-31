package com.timekeeper.Database.Repository

import android.arch.lifecycle.LiveData
import android.support.annotation.WorkerThread
import com.timekeeper.Database.DAO.ActivityDao
import com.timekeeper.Database.Entity.Activity

class ActivityRepository(private val activityDao: ActivityDao) {
    val allActivities: LiveData<List<Activity>> = activityDao.getAllActivities()

    @WorkerThread
    suspend fun insert(activity: Activity){
        activityDao.insert(activity)
    }
}