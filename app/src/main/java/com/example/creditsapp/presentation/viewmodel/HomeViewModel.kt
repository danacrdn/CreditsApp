package com.example.creditsapp.presentation.viewmodel

import androidx.lifecycle.ViewModel
import com.example.creditsapp.domain.model.Activity
import com.example.creditsapp.domain.model.userActivities

class HomeViewModel : ViewModel() {

    private fun getActivitiesForUser(userId: Int): List<Activity> {
        return userActivities[userId] ?: emptyList()
    }

    fun getTotalCreditsForUser(userId: Int): Int{
        return getActivitiesForUser(userId).sumOf { it.creditValue }
    }
}