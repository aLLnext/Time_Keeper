package com.timekeeper.Database.Entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import com.timekeeper.Model.Condition

@Entity(tableName = "activities")
class Activity(@PrimaryKey @ColumnInfo(name = "id") val id: Int,
               @ColumnInfo(name = "name") var name: String,
               @ColumnInfo(name = "condition") var condition: Int,
               @ColumnInfo(name = "timer_base") var timer_base: Long,
               @ColumnInfo(name = "current_time") var current_time: Long,
               @ColumnInfo(name = "comment") var comment: String?) {
    /*fun fromDB(): MyActivity {
        val tmp = MyActivity(name, id)
        tmp.comment = comment
        tmp.timerBase = timer_base
        tmp.currentTime = current_time
        tmp.condition = if (condition == 0) Condition.ACTIVE else Condition.ACTIVE
        return tmp
    }*/

    object Supplier {
        val activities = listOf(
                Activity(0, "Sleeping", 1, 0, 0, null),
                Activity(1, "Coding", 1, 0, 0, null)
        )
    }
}
