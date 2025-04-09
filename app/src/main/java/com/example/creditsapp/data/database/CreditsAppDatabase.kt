package com.example.creditsapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Activity::class, User::class, UserActivity::class], version = 1, exportSchema = false)
abstract class CreditsAppDatabase : RoomDatabase() {
    abstract fun activityDao(): ActivityDao
    abstract fun userDao(): UserDao
    abstract fun userActivityDao(): UserActivityDao

    companion object {
        @Volatile
        private var Instance: CreditsAppDatabase? = null

        fun getDatabase(context: Context): CreditsAppDatabase {
            return Instance ?: synchronized(this){
                Room.databaseBuilder(
                    context, CreditsAppDatabase::class.java, "credits_app_database"
                ).build().also { Instance = it }
            }
        }
    }
}