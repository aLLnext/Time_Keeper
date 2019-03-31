package com.timekeeper.Database

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.timekeeper.Database.DAO.ActivityDao
import com.timekeeper.Database.Entity.Activity
import com.timekeeper.Model.Supplier
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Activity::class], version = 1)
abstract class ActivityRoomDatabase : RoomDatabase() {

    abstract fun activityDao(): ActivityDao

    companion object {
        @Volatile
        private var INSTANCE: ActivityRoomDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): ActivityRoomDatabase {
            val tempInstance = INSTANCE
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        ActivityRoomDatabase::class.java,
                        "Activity_database"
                ).addCallback(ActivityDatabaseCallback(scope))
                        .build()
                INSTANCE = instance
                return instance
            }
        }
    }

    private class ActivityDatabaseCallback(
            private val scope: CoroutineScope)
        : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    populateDatabase(database.activityDao())
                }
            }
        }

        fun populateDatabase(activityDao: ActivityDao) {
            activityDao.deleteAll()
            with(Supplier.activities[0]) {
                val act = Activity(id, name, condition.ordinal, timerBase, currentTime, null)
                activityDao.insert(act)
            }
            with(Supplier.activities[1]) {
                val act = Activity(id, name, condition.ordinal, timerBase, currentTime, null)
                activityDao.insert(act)
            }

        }
    }
}