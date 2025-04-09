package com.example.creditsapp.data.repository

import com.example.creditsapp.data.database.Activity
import com.example.creditsapp.data.database.ActivityDao
import kotlinx.coroutines.flow.Flow

interface ActivitiesRepository {
    fun getAllActivitiesStream(): Flow<List<Activity>>

    fun getActivityStream(id: Int): Flow<Activity>

    suspend fun insertItem(activity: Activity)

    suspend fun updateItem(activity: Activity)

    suspend fun deleteItem(activity: Activity)
}

class DefaultActivitiesRepository(private val activityDao: ActivityDao) : ActivitiesRepository {
    override fun getAllActivitiesStream(): Flow<List<Activity>> = activityDao.getAllActivities()

    override fun getActivityStream(id: Int): Flow<Activity> = activityDao.getActivity(id)

    override suspend fun insertItem(activity: Activity) = activityDao.insert(activity)

    override suspend fun updateItem(activity: Activity) = activityDao.update(activity)

    override suspend fun deleteItem(activity: Activity) = activityDao.delete(activity)

}