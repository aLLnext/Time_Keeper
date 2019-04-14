package com.timekeeper.Database

import android.arch.persistence.db.SupportSQLiteDatabase
import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.timekeeper.Database.DAO.ActivityDao
import com.timekeeper.Database.Entity.Activity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import android.arch.persistence.room.migration.Migration
import com.timekeeper.Database.DAO.StatusDao
import com.timekeeper.Database.Entity.Status


@Database(entities = [Activity::class, Status::class], version = 3)
abstract class ActivityRoomDatabase : RoomDatabase() {

    abstract fun activityDao(): ActivityDao
    abstract fun statusDao(): StatusDao

    companion object {
        @Volatile
        private var INSTANCE: ActivityRoomDatabase? = null

        val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL("ALTER TABLE activities ADD COLUMN saved INTEGER DEFAULT 0 NOT NULL")
            }
        }


        fun getDatabase(context: Context, scope: CoroutineScope): ActivityRoomDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                        context.applicationContext,
                        ActivityRoomDatabase::class.java,
                        "testDB"
                ).fallbackToDestructiveMigration() //удаляет все при изменении базы
                        .addCallback(ActivityDatabaseCallback(scope))
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
//            INSTANCE?.let { database ->
//                scope.launch(Dispatchers.IO) {
//                    populateDatabase(database.activityDao(), database.statusDao())
//                }
//            }
        }

        fun populateDatabase(activityDao: ActivityDao, statusDao: StatusDao) {
            activityDao.deleteAll()
            statusDao.deleteAll()
            val status1 = Status(0, 0, 0, 0)
            val status2 = Status(1, 0, 0, 0)
            statusDao.insert(status1)
            statusDao.insert(status2)
            val act1 = Activity(0, "Coding", null, 0)
            activityDao.insert(act1)
            val act2 = Activity(1, "Sleeping", null, 1)
            activityDao.insert(act2)
        }
    }
}