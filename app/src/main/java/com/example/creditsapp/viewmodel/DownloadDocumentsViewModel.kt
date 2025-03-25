package com.example.creditsapp.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.creditsapp.data.Activity
import com.example.creditsapp.data.activities

class DownloadDocumentsViewModel : ViewModel() {
    private val _activities = activities
    private val _completedActivities = mutableStateOf<List<Activity>>(emptyList())

    val completedActivities = _completedActivities

    fun filterCompletedActivities(){
        _completedActivities.value = _activities.filter { it.completed }
    }
}