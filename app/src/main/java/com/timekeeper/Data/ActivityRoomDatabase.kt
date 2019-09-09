package com.timekeeper.Data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.timekeeper.Data.DAO.ActivityDao
import com.timekeeper.Data.Entity.Activity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Activity::class], version = 1)
abstract class ActivityRoomDatabase : RoomDatabase() {

    abstract fun activityDao(): ActivityDao

    companion object {
        @Volatile
        private var INSTANCE: ActivityRoomDatabase? = null

        val MIGRATION_1_1: Migration = object : Migration(1, 1) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("")
            }
        }

        fun getDatabase(context: Context, scope: CoroutineScope): ActivityRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val intsnce = Room.databaseBuilder(
                    context.applicationContext,
                    ActivityRoomDatabase::class.java,
                    "Local_database"
                ).fallbackToDestructiveMigration() // удаляет все при изменении базы
                    .addCallback(ActivityDatabaseCallback(scope))
                    .build()
                INSTANCE = intsnce
                return intsnce
            }
        }
    }

    private class ActivityDatabaseCallback
        (private val scope: CoroutineScope) : RoomDatabase.Callback() {
        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch(Dispatchers.IO) {
                    //populateDatabase(database.activityDao())
                }
            }
        }


        fun populateDatabase(activityDao: ActivityDao) {
            activityDao.deleteAll()
            val act1 = Activity(0, "Coding", "", 0, 0)
            activityDao.insert(act1)
            val act2 = Activity(1, "Sleeping", "", 0, 0)
            activityDao.insert(act2)
        }
    }
}