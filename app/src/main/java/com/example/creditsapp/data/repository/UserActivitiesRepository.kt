package com.example.creditsapp.data.repository

import com.example.creditsapp.data.database.Activity
import com.example.creditsapp.data.database.UserActivity
import com.example.creditsapp.data.database.UserActivityDao
import com.example.creditsapp.domain.model.UserTotalCredits
import kotlinx.coroutines.flow.Flow

interface UserActivitiesRepository {

    suspend fun deleteActivityUserByIds(activityId: Int, userId: Int)

    suspend fun getActivityStatusForUserStream(activityId: Int, userId: Int): UserActivity

    fun getCompletedActivitiesForUserStream(id: Int): Flow<List<Activity>>

    fun getActivitiesForUserStream(id: Int): Flow<List<Activity>>

    suspend fun insertUserActivity(userActivity: UserActivity)

    suspend fun updateUserActivity(userActivity: UserActivity)

    suspend fun deleteUserActivity(userActivity: UserActivity)
}

class DefaultUserActivitiesRepository(private val userActivityDao: UserActivityDao) :
    UserActivitiesRepository {
    override fun getActivitiesForUserStream(id: Int): Flow<List<Activity>> =
        userActivityDao.getActivitiesForUser(id)

    override suspend fun deleteActivityUserByIds(activityId: Int, userId: Int) =
        userActivityDao.deleteActivityUserByIds(activityId, userId)

    override suspend fun getActivityStatusForUserStream(activityId: Int, userId: Int): UserActivity =
        userActivityDao.getActivityStatusForUser(activityId, userId)

    override fun getCompletedActivitiesForUserStream(id: Int): Flow<List<Activity>> =
        userActivityDao.getCompletedActivitiesForUser(id)

    override suspend fun insertUserActivity(userActivity: UserActivity) =
        userActivityDao.insert(userActivity)

    override suspend fun updateUserActivity(userActivity: UserActivity) =
        userActivityDao.update(userActivity)

    override suspend fun deleteUserActivity(userActivity: UserActivity) =
        userActivityDao.delete(userActivity)

}