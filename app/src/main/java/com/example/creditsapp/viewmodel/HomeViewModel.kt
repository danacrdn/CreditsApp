package com.example.creditsapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.creditsapp.data.Activity
import com.example.creditsapp.data.userActivities

class HomeViewModel : ViewModel() {

    private fun getActivitiesForUser(userId: Int): List<Activity> {
        return userActivities[userId] ?: emptyList()
    }

    fun getTotalCreditsForUser(userId: Int): Int{
        return getActivitiesForUser(userId).sumOf { it.creditValue }
    }
}