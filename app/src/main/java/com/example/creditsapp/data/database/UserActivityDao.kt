package com.example.creditsapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.creditsapp.domain.model.UserTotalCredits
import kotlinx.coroutines.flow.Flow

@Dao
interface UserActivityDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(userActivity: UserActivity)

    @Update
    suspend fun update(userActivity: UserActivity)

    @Delete
    suspend fun delete(userActivity: UserActivity)

    @Query(
        "SELECT a.* from activities a " +
                "INNER JOIN user_activity ua ON a.id = ua.activityId " +
                "WHERE ua.userId = :id"
    )
    fun getActivitiesForUser(id: Int): Flow<List<Activity>>

    @Query(
        "SELECT a.* from activities a " +
                "INNER JOIN user_activity ua ON a.id = ua.activityId " +
                "WHERE ua.userId = :id AND ua.completed = 1"
    )
    fun getCompletedActivitiesForUser(id: Int): Flow<List<Activity>>

    @Query(
        "SELECT users.firstName AS name, COALESCE(SUM(activities.value), 0) AS totalCredits from user_activity " +
                "INNER JOIN users ON users.id = user_activity.userId " +
                "INNER JOIN activities ON activities.id = user_activity.activityId " +
                "WHERE user_activity.completed = 1 AND users.id = :id"
    )
    fun getUserAndTotalCredits(id: Int): Flow<UserTotalCredits>
}