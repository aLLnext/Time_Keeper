package com.timekeeper.Database.DAO

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.timekeeper.Database.Entity.Activity
import com.timekeeper.Database.Entity.Status

@Dao
interface ActivityDao {
    @Query("SELECT * FROM activities ORDER BY id")
    fun getAllActivities():LiveData<List<Activity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(activity: Activity)

    @Update
    fun update(activity: Activity)

    @Query("DELETE FROM activities WHERE id = :id")
    fun deleteActivity(id: Int)

    @Delete
    fun delete(activity: Activity)

    @Query("DELETE FROM activities")
    fun deleteAll()
}

