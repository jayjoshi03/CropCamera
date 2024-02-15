package com.example.cropcamera.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.cropcamera.databinding.ActivityCameraBinding
import com.example.cropcamera.viewmodel.ImageViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
/**
 * Activity for capturing images using the device's camera.
 * This activity provides the functionality to capture images, crop them, and save them to local storage.
 */
@AndroidEntryPoint
class CameraActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCameraBinding
    private lateinit var cameraExecutor: ExecutorService
    private var imageCapture: ImageCapture? = null
    private val viewModel: ImageViewModel by viewModels()

    companion object {
        const val TAG = "CamTest"
        private const val REQUEST_CAMERA_PERMISSION = 200
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCameraBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
            == PackageManager.PERMISSION_GRANTED
        ) {
            openCamera()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                REQUEST_CAMERA_PERMISSION
            )
        }
        cameraExecutor = Executors.newSingleThreadExecutor()
        binding.btnCapture.setOnClickListener {
            takeImage()
        }

    }
    /**
     * Method to capture an image using the camera.
     * Captures the image, crops it, rotates it, saves it to local storage, and opens the preview activity.
     */
    private fun takeImage() {
        val imageCapture = imageCapture ?: return

        imageCapture.takePicture(ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    val bitmap = image.toBitmap()

                    val photoFile = SimpleDateFormat(FILENAME_FORMAT, Locale.US)
                        .format(System.currentTimeMillis()) + ".jpg"

                    // Calculate the cropping area
                    val centerX = bitmap.width / 2 // X coordinate of the center
                    val centerY = bitmap.height / 2 // Y coordinate of the center
                    val cropWidth = 500 // Width of the cropping area
                    val cropHeight = 500 // Height of the cropping area

                    // Calculate the cropping area boundaries
                    val cropLeft = centerX - cropWidth / 2
                    val cropTop = centerY - cropHeight / 2
                    val cropRight = centerX + cropWidth / 2
                    val cropBottom = centerY + cropHeight / 2

// Check if the cropping area is valid
                    if (cropLeft >= 0 && cropTop >= 0 && cropRight <= bitmap.width && cropBottom <= bitmap.height) {
                        // Crop the image
                        val croppedBitmap =
                            Bitmap.createBitmap(bitmap, cropLeft, cropTop, cropWidth, cropHeight)

                        // Rotate the cropped bitmap based on the rotation degree
                        val matrix = Matrix().apply {
                            postRotate(image.imageInfo.rotationDegrees.toFloat())
                        }
                        val rotatedBitmap = Bitmap.createBitmap(
                            croppedBitmap,
                            0,
                            0,
                            croppedBitmap.width,
                            croppedBitmap.height,
                            matrix,
                            true
                        )

                        // Save the cropped and rotated bitmap to a file
                        viewModel.saveImage(rotatedBitmap, photoFile)
                        //Image Path Share
                        val intent = Intent(this@CameraActivity, PreviewActivity::class.java)
                        intent.putExtra("PATH", photoFile)
                        startActivity(intent)
                    } else {
                        Log.e(TAG, "Invalid cropping coordinates")
                    }
                    // Close the ImageProxy
                    image.close()
                }

                override fun onError(exception: ImageCaptureException) {
                    super.onError(exception)
                    Log.e("Camera", "Couldn't take photo: ", exception)
                }

            })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == REQUEST_CAMERA_PERMISSION &&
            grantResults.isNotEmpty() &&
            grantResults[0] == PackageManager.PERMISSION_GRANTED
        ) {
            openCamera()
        }
    }

    private fun openCamera() {
        val cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider: ProcessCameraProvider = cameraProviderFuture.get()

            val preview = Preview.Builder()
                .build()
                .also {
                    it.setSurfaceProvider(binding.viewFinder.surfaceProvider)
                }


            imageCapture = ImageCapture.Builder()
                .setCaptureMode(ImageCapture.CAPTURE_MODE_MAXIMIZE_QUALITY)
                .build()

            val imageAnalyzer = ImageAnalysis.Builder()
                .build()
                .also {
                    it.setAnalyzer(cameraExecutor, LuminosityAnalyzer { luma ->
                        Log.d(TAG, "Average luminosity: $luma")
                    })
                }

            val cameraSelector = CameraSelector.DEFAULT_BACK_CAMERA

            try {
                cameraProvider.unbindAll()
                cameraProvider.bindToLifecycle(
                    this, cameraSelector, preview, imageCapture, imageAnalyzer
                )
            } catch (exc: Exception) {
                Log.e(TAG, "Use case binding failed", exc)
            }
        }, ContextCompat.getMainExecutor(this))
    }

    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, MainActivity::class.java))
    }

}