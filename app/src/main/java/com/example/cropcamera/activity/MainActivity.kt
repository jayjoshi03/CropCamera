package com.example.cropcamera.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cropcamera.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main activity of the CropCamera app.
 * This activity serves as the entry point to the application and handles permissions.
 */
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    /** Binding object for the main activity layout */
    private lateinit var binding: ActivityMainBinding
    /** Request code for permissions */
    private val PERMISSION_REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnOpenCamera.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }

        // Check permissions
        if (!checkPermissions())
            requestPermissions()
    }

    /**
     * Checks if required permissions are granted.
     * @return True if all permissions are granted, false otherwise.
     */
    private fun checkPermissions(): Boolean {
        // Check if camera permission is granted
        val cameraPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        // Check if location permissions are granted
        val coarseLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
        val fineLocationPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        // Check if all permissions are granted
        return cameraPermission && coarseLocationPermission && fineLocationPermission
    }

    /**
     * Requests permissions for the required features of the app.
     */

    private fun requestPermissions() {
        // Request permissions
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.CAMERA,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            PERMISSION_REQUEST_CODE
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Check if permission request was for our request code
        if (requestCode == PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults.all { it == PackageManager.PERMISSION_GRANTED }) {

            } else {
                Toast.makeText(this, "Permissions not granted by the user.", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }
}