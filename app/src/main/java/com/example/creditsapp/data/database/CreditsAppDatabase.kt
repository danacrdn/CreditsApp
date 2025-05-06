package com.example.creditsapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Database(entities = [Activity::class, User::class, UserActivity::class], version = 2, exportSchema = false)
abstract class CreditsAppDatabase : RoomDatabase() {
    abstract fun activityDao(): ActivityDao
    abstract fun userDao(): UserDao
    abstract fun userActivityDao(): UserActivityDao

    companion object {
        @Volatile
        private var Instance: CreditsAppDatabase? = null

        private val MIGRATION_1_2 = object : Migration(1, 2) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE users ADD COLUMN password TEXT NOT NULL DEFAULT ''")
            }
        }

        fun getDatabase(context: Context): CreditsAppDatabase {
            return Instance ?: synchronized(this){
                Room.databaseBuilder(
                    context, CreditsAppDatabase::class.java, "credits_app_database"
                )
                    .addMigrations(MIGRATION_1_2)
                    .build().also { Instance = it }
            }
        }
    }
}