package com.sushilbx.navgurukulam

import android.app.Application
import androidx.work.Configuration
import androidx.work.WorkManager

class App : Application() {
    override fun onCreate() {
        super.onCreate()

        val config = Configuration.Builder()
            .setMinimumLoggingLevel(android.util.Log.DEBUG)
            .build()

        WorkManager.initialize(this, config)
    }
}
