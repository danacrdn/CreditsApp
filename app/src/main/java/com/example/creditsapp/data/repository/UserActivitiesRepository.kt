package com.example.creditsapp.data.repository

import com.example.creditsapp.data.database.Activity
import com.example.creditsapp.data.database.UserActivity
import com.example.creditsapp.data.database.UserActivityDao
import com.example.creditsapp.domain.model.UserTotalCredits
import kotlinx.coroutines.flow.Flow

interface UserActivitiesRepository {
    fun getCompletedActivitiesForUserStream(id: Int): Flow<List<Activity>>

    fun getActivitiesForUserStream(id: Int): Flow<List<Activity>>

    fun getUserAndTotalCreditsStream(id: Int): Flow<UserTotalCredits>

    suspend fun insertUserActivity(userActivity: UserActivity)

    suspend fun updateUserActivity(userActivity: UserActivity)

    suspend fun deleteUserActivity(userActivity: UserActivity)
}

class DefaultUserActivitiesRepository(private val userActivityDao: UserActivityDao) :
    UserActivitiesRepository {
    override fun getActivitiesForUserStream(id: Int): Flow<List<Activity>> =
        userActivityDao.getActivitiesForUser(id)

    override fun getUserAndTotalCreditsStream(id: Int): Flow<UserTotalCredits> =
        userActivityDao.getUserAndTotalCredits(id)

    override fun getCompletedActivitiesForUserStream(id: Int): Flow<List<Activity>> =
        userActivityDao.getCompletedActivitiesForUser(id)

    override suspend fun insertUserActivity(userActivity: UserActivity) =
        userActivityDao.insert(userActivity)

    override suspend fun updateUserActivity(userActivity: UserActivity) =
        userActivityDao.update(userActivity)

    override suspend fun deleteUserActivity(userActivity: UserActivity) =
        userActivityDao.delete(userActivity)

}