package com.timekeeper.Database.Entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
class Status(@PrimaryKey val id: Int,
             @ColumnInfo(name = "current_time") var current_time: Long,
             @ColumnInfo(name = "timer_base") var timer_base: Long,
             @ColumnInfo(name = "condition") var condition: Int)