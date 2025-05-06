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
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Update
    suspend fun update(user: User)

    @Delete
    suspend fun delete(user: User)

    @Query("SELECT * from users WHERE id = :id")
    fun getUser(id: Int): Flow<User>

    @Query(
        "SELECT users.firstName AS name, COALESCE(SUM(activities.value), 0) AS totalCredits " +
                "FROM users " +
                "LEFT JOIN user_activity ON users.id = user_activity.userId AND user_activity.completed = 1 " +
                "LEFT JOIN activities ON activities.id = user_activity.activityId " +
                "WHERE users.id = :id"
    )
    fun getUserAndCredits(id: Int): Flow<UserTotalCredits>

    @Query("SELECT * from users WHERE email = :email")
    fun getUserByEmail(email: String): Flow<User>
}