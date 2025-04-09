package com.example.creditsapp.data.database

import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    tableName = "user_activity",
    primaryKeys = ["activityId", "userId"],
    foreignKeys = [
        ForeignKey(entity = User::class, parentColumns = ["id"], childColumns = ["userId"]),
        ForeignKey(entity = Activity::class, parentColumns = ["id"], childColumns = ["activityId"])
    ]
)
data class UserActivity(
    val activityId: Int,
    val userId: Int,
    val completed: Boolean
)
