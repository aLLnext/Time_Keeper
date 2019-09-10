package com.timekeeper.Data.DAO

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import com.timekeeper.Data.Entity.Activity

@Dao
interface ActivityDao {
    @Query("SELECT * FROM activity ORDER BY id")
    fun getAllActivities(): LiveData<List<Activity>>

    @Query("SELECT * FROM activity WHERE id = :id")
    fun getActivityById(id: Int): LiveData<Activity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(activity: Activity)

    @Update
    fun update(activity: Activity)

    @Query("DELETE FROM activity WHERE id = :id")
    fun deleteById(id: Int)

    @Delete
    fun delete(activity: Activity)

    @Query("DELETE FROM activity")
    fun deleteAll()
}