package com.example.creditsapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
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
        "SELECT * FROM user_activity WHERE activityId = :activityId and userId = :userId"
    )
    suspend fun getActivityStatusForUser(activityId: Int, userId: Int): UserActivity

    @Query(
        "DELETE FROM user_activity WHERE activityId = :activityId and userId = :userId"
    )
    suspend fun deleteActivityUserByIds(activityId: Int, userId: Int)
}