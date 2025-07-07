package com.example.creditsapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date


@Entity(tableName = "activities")
data class Activity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val name: String,
    val date: Date,
    val hour: String,
    val place: String,
    val spots: Int,
    val value: Double
)
