package com.timekeeper.Model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.timekeeper.Data.ActivityRoomDatabase
import com.timekeeper.Data.Entity.Activity
import com.timekeeper.Data.Repository.ActivityRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ActivityViewModel(application: Application) : AndroidViewModel(application) {


    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    val DB: ActivityRoomDatabase = ActivityRoomDatabase.getDatabase(application, scope)

    private val repositoryAct = ActivityRepository(DB.activityDao())

    val allActivity: LiveData<List<Activity>> = repositoryAct.allActivities

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun insertActivity(activity: Activity) = scope.launch(Dispatchers.IO) {
        repositoryAct.insertActivity(activity)
    }

    fun updateActivity(activity: Activity) = scope.launch(Dispatchers.IO) {
        repositoryAct.updateActivity(activity)
    }

    fun deleteActivity(activity: Activity) = scope.launch(Dispatchers.IO) {
        repositoryAct.deleteActivity(activity)
    }
}