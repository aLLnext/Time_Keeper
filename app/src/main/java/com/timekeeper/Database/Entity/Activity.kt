package com.timekeeper.Database.Entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity(tableName = "activities")
class Activity(@PrimaryKey @ColumnInfo(name = "id") val id: Int,
               @ColumnInfo(name = "name") val name: String,
               @ColumnInfo(name = "condition") val condition: Int,
               @ColumnInfo(name = "timer_base") val timer_base: Long,
               @ColumnInfo(name = "current_time") val current_time: Long,
               @ColumnInfo(name = "comment") val comment: String?)