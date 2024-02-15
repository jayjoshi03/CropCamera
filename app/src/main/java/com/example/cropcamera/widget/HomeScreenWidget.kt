package com.example.cropcamera.widget

import android.annotation.SuppressLint
import android.app.ActivityManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.widget.RemoteViews
import com.example.cropcamera.R
import com.example.cropcamera.activity.CameraActivity
import com.example.cropcamera.service.LocationService

/**
 * AppWidgetProvider for the home screen widget.
 */
class HomeScreenWidget : AppWidgetProvider() {
    companion object {
        private const val ACTION_CLICK_IMAGE = "ACTION_CLICK_IMAGE"
        private const val ACTION_SWITCH_TOGGLED = "ACTION_SWITCH_TOGGLED"
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        // There may be multiple widgets active, so update all of them
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_CLICK_IMAGE) {
            // Open the app and go to the camera activity
            val appIntent = Intent(context, CameraActivity::class.java)
            appIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(appIntent)
        } else if (intent.action == ACTION_SWITCH_TOGGLED) {
            val isLocationServiceRunning = isLocationServiceRunning(context!!)
            if (isLocationServiceRunning) {
                stopLocationService(context)
            } else {
                startLocationService(context)
            }
        }
    }

    @SuppressLint("RemoteViewLayout")
    private fun updateAppWidget(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetId: Int
    ) {
        // Construct the RemoteViews object
        val views = RemoteViews(context.packageName, R.layout.home_screen_widget)

        // Set up click listener for "Click Image" button
        val clickImageIntent = Intent(context, HomeScreenWidget::class.java)
        clickImageIntent.action = ACTION_CLICK_IMAGE
        val clickImagePendingIntent = PendingIntent.getBroadcast(
            context, 0, clickImageIntent,
            PendingIntent.FLAG_MUTABLE
        )
        views.setOnClickPendingIntent(R.id.clickImage, clickImagePendingIntent)


        // Set up the click action for the button
        val intent = Intent(context, HomeScreenWidget::class.java)
        intent.action = ACTION_SWITCH_TOGGLED
        val pendingIntent = PendingIntent.getBroadcast(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        views.setOnClickPendingIntent(R.id.locationSwitch, pendingIntent)

        appWidgetManager.updateAppWidget(appWidgetId, views)
    }

    private fun startLocationService(context: Context) {
        val serviceIntent = Intent(context, LocationService::class.java)
        serviceIntent.action = LocationService.ACTION_START
        context.startService(serviceIntent)
    }

    private fun stopLocationService(context: Context) {
        val serviceIntent = Intent(context, LocationService::class.java)
        serviceIntent.action = LocationService.ACTION_STOP
        context.stopService(serviceIntent)
    }

    private fun isLocationServiceRunning(context: Context): Boolean {
        val activityManager = context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val runningServices = activityManager.getRunningServices(Integer.MAX_VALUE)
        for (serviceInfo in runningServices) {
            if (LocationService::class.java.name == serviceInfo.service.className) {
                // Found the location service running
                return true
            }
        }
        return false
    }

}