package com.timekeeper.Database.Entity

import android.arch.persistence.room.*

@Entity(tableName = "activities", foreignKeys =
[
    ForeignKey(
            entity = Status::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("statusId"),
            onDelete = ForeignKey.CASCADE
    )
], indices = [Index(value = ["statusId"])]
)
class Activity(@PrimaryKey val id: Int,
               @ColumnInfo(name = "name") var name: String,
               @ColumnInfo(name = "comment") var comment: String?,
               @ColumnInfo(name = "statusId") var statusId: Int)
               //TODO ЩАС БУДЕТ КОСТЫЛЬ
               //@ColumnInfo(name = "saved") var saved: Int)
