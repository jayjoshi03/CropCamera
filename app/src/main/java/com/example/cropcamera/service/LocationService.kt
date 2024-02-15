package com.example.cropcamera.service

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.example.cropcamera.R
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import java.io.File
import java.io.FileWriter
import java.io.IOException

/**
 * Service responsible for tracking the device's location and writing it to a CSV file.
 */
class LocationService : Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)
    private lateinit var locationClient: LocationClient

    private val locationList = ArrayList<Pair<Double, Double>>()

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        locationClient = DefaultLocationClient(
            applicationContext, LocationServices.getFusedLocationProviderClient(applicationContext)
        )
        writeLocationToCSV()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {
        val notification =
            NotificationCompat.Builder(this, "location").setContentTitle("Tracking location...")
                .setContentText("Location: null").setSmallIcon(R.drawable.ic_launcher_background)
                .setOngoing(true)

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        locationClient.getLocationUpdates(2000L).catch { e -> e.printStackTrace() }
            .onEach { location ->
                val lat = location.latitude.toString().takeLast(3)
                val long = location.longitude.toString().takeLast(3)
                val updatedNotification = notification.setContentText(
                    "Location: ($lat, $long)"
                )
                locationList.add(location.latitude to location.longitude)
                notificationManager.notify(1, updatedNotification.build())
            }.launchIn(serviceScope)

        startForeground(1, notification.build())
    }

    private fun writeLocationToCSV() {
        val fileName = "LocationData_list.csv"
        val file = File(getOutputDirectory(), fileName)
        try {
            FileWriter(file).use { writer ->
                locationList.forEach { (lat, long) ->
                    writer.append("$lat,$long\n")
                }
                writer.flush()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }

    }

    private fun getOutputDirectory(): File {
        val mediaDir = externalMediaDirs.firstOrNull()?.let {
            File(it, resources.getString(R.string.app_name)).apply { mkdirs() }
        }
        return if (mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }

    private fun stop() {
        stopForeground(true)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"
    }
}