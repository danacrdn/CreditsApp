package com.example.creditsapp

import android.app.Application
import com.example.creditsapp.data.AppContainer
import com.example.creditsapp.data.DefaultAppContainer

class CreditsApp : Application() {
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(this)
        println("App onCreate - this: $this")
    }
}