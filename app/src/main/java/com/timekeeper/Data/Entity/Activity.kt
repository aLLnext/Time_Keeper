package com.timekeeper.Data.Entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.timekeeper.Timer.TimerActivity

@Entity
class Activity(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "name") var name: String,
    @ColumnInfo(name = "comment") var comment: String,
    @ColumnInfo(name = "timer_base") var timer_base: Long,
    @ColumnInfo(name = "all_time") var all_time: Long,
    @ColumnInfo(name = "status") var status: Int
)