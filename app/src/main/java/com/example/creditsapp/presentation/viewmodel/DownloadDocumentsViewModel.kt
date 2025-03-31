package com.example.creditsapp.presentation.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.creditsapp.domain.model.Activity
import com.example.creditsapp.domain.model.activities

class DownloadDocumentsViewModel : ViewModel() {
    private val _activities = activities
    private val _completedActivities = mutableStateOf<List<Activity>>(emptyList())

    val completedActivities = _completedActivities

    fun filterCompletedActivities(){
        _completedActivities.value = _activities.filter { it.completed }
    }
}