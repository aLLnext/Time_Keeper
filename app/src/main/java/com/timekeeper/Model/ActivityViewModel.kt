package com.timekeeper.Model

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.widget.Toast
import com.timekeeper.Database.ActivityRoomDatabase
import com.timekeeper.Database.Entity.Activity
import com.timekeeper.Database.Repository.ActivityRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val repository: ActivityRepository
    internal var allActivity: LiveData<List<Activity>>

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main

    private val scope = CoroutineScope(coroutineContext)

    init {
        val activityDao = ActivityRoomDatabase.getDatabase(application, scope).activityDao()
        repository = ActivityRepository(activityDao)
        allActivity = repository.allActivities
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun insert(activity: Activity) = scope.launch(Dispatchers.IO) {
        repository.insert(activity)
        print("SUCCESS")
    }
}