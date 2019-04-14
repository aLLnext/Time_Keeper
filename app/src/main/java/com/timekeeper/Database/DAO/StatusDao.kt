package com.timekeeper.Database.DAO

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.*
import com.timekeeper.Database.Entity.Activity
import com.timekeeper.Database.Entity.Status

@Dao
interface StatusDao {
    @Query("SELECT * FROM status ORDER BY id")
    fun getAllStatuses(): List<Status>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(status: Status)

    @Query("SELECT * FROM status s JOIN activities a ON s.id = :status_id")
    fun getStatus(status_id: Int): Status

    @Update
    fun updateStatus(status: Status)

    @Query("DELETE FROM status")
    fun deleteAll()
}