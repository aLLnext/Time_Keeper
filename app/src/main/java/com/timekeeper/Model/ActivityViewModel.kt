package com.timekeeper.Model

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.util.Log
import android.widget.Toast
import com.timekeeper.Database.ActivityRoomDatabase
import com.timekeeper.Database.Entity.Activity
import com.timekeeper.Database.Entity.Status
import com.timekeeper.Database.Repository.ActivityRepository
import com.timekeeper.Database.Repository.StatusRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlin.coroutines.CoroutineContext

class ActivityViewModel(application: Application) : AndroidViewModel(application) {
    private val repositoryAct: ActivityRepository
    private val repositoryStat: StatusRepository

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Main
    private val scope = CoroutineScope(coroutineContext)

    val DB: ActivityRoomDatabase
    val allActivity: LiveData<List<Activity>>

    init {
        DB = ActivityRoomDatabase.getDatabase(application, scope)
        repositoryAct = ActivityRepository(DB.activityDao())
        repositoryStat = StatusRepository(DB.statusDao())
        allActivity = repositoryAct.allActivities
    }

    override fun onCleared() {
        super.onCleared()
        parentJob.cancel()
    }

    fun insertStatus(status: Status) = scope.launch(Dispatchers.IO) {
        repositoryStat.insertStat(status)
    }

    fun insert(activity: Activity) = scope.launch(Dispatchers.IO) {
        repositoryAct.insert(activity)
        Log.d("insert", "Success INSERT")
    }

    fun updateStatus(status: Status) = scope.launch(Dispatchers.IO) {
        repositoryStat.updateStatus(status)
        Log.d("update", "Success STATUS UPDATE")
    }

    fun getStatus(activity: Activity): Status {
        return repositoryStat.getStatus(activity.statusId)
    }

    /*fun update(activity: Activity) = scope.launch(Dispatchers.IO) {
        repository.update(activity)
        Log.d("update", "Success UPDATE")
    }*/
}