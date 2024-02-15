package com.example.cropcamera.activity

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import dagger.hilt.android.HiltAndroidApp


/**
 * Application class for initializing the main application context and creating notification channels.
 * This class is annotated with @HiltAndroidApp to enable Hilt for dependency injection.
 */
@HiltAndroidApp
class MainApp : Application() {

    /**
     * Called when the application is starting.
     * Creates a notification channel for location updates if the device's SDK version is Oreo or higher.
     */
    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "location",
                "Location",
                NotificationManager.IMPORTANCE_LOW
            )
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}