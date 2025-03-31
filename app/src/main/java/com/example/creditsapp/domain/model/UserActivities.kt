package com.example.creditsapp.domain.model


data class UserActivities (
    val userId: User,
    val activityId: List <Activity>
)

val userActivities = mapOf(
    1 to listOf(activities[0], activities[1]),
    2 to listOf(activities[1], activities[2]),
    3 to listOf(activities[0], activities[2])
)