package com.example.creditsapp.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import java.text.SimpleDateFormat
import java.util.Locale

@Database(
    entities = [Activity::class, User::class, UserActivity::class],
    version = 3,
    exportSchema = false
)
@TypeConverters(Converters::class)
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

        val MIGRATION_2_3 = object : Migration(2, 3) {
            override fun migrate(db: SupportSQLiteDatabase) {
                db.execSQL(
                    """
                    CREATE TABLE activities_new (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        name TEXT NOT NULL,
                        date INTEGER NOT NULL,
                        hour TEXT NOT NULL,
                        place TEXT NOT NULL,
                        spots INTEGER NOT NULL,
                        value REAL NOT NULL
                    )
                """.trimIndent()
                )

                val cursor = db.query("SELECT * FROM activities")
                val formatter = SimpleDateFormat("d 'de' MMMM, yyyy", Locale("es", "ES"))

                while (cursor.moveToNext()) {
                    val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
                    val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
                    val dateStr = cursor.getString(cursor.getColumnIndexOrThrow("date"))
                    val hour = cursor.getString(cursor.getColumnIndexOrThrow("hour"))
                    val place = cursor.getString(cursor.getColumnIndexOrThrow("place"))
                    val spots = cursor.getInt(cursor.getColumnIndexOrThrow("spots"))
                    val value = cursor.getDouble(cursor.getColumnIndexOrThrow("value"))

                    val dateLong = formatter.parse(dateStr)?.time ?: 0L

                    db.execSQL(
                        """
                        INSERT INTO activities_new (id, name, date, hour, place, spots, value)
                        VALUES (?, ?, ?, ?, ?, ?, ?)
                        """, arrayOf(id, name, dateLong, hour, place, spots, value)
                    )
                }
                cursor.close()

                db.execSQL("DROP TABLE activities")
                db.execSQL("ALTER TABLE activities_new RENAME TO activities")
            }

        }

        fun getDatabase(context: Context): CreditsAppDatabase {
            return Instance ?: synchronized(this) {
                Room.databaseBuilder(
                    context, CreditsAppDatabase::class.java, "credits_app_database"
                )
                    .addMigrations(MIGRATION_1_2, MIGRATION_2_3)
                    .build().also { Instance = it }
            }
        }
    }
}