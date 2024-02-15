package com.example.cropcamera.activity

import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.cropcamera.databinding.ActivityPreviewBinding
import com.example.cropcamera.viewmodel.ImageViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * Activity to preview the captured image.
 * This activity displays the captured image loaded from the provided image path.
 */
@AndroidEntryPoint
class PreviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityPreviewBinding
    private val viewModel: ImageViewModel by viewModels()

    /**
     * The path of the image to be displayed.
     */
    private var pathImage = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPreviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        pathImage = intent.getStringExtra("PATH").toString()

        viewModel.imagePath(pathImage).observe(this) { imagePath ->
            if (imagePath != null) {
                // Load image from the provided image path
                val bitmap = BitmapFactory.decodeFile(imagePath)
                binding.imageView.setImageBitmap(bitmap)
            } else {
                Toast.makeText(this, "Image not found", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnBackCamera.setOnClickListener {
            startActivity(Intent(this, CameraActivity::class.java))
        }
    }

    /**
     * Deprecated method overriding the back button press action.
     * This method is deprecated in favor of the onBackPressed method.
     */

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        super.onBackPressed()
        startActivity(Intent(this, CameraActivity::class.java))
    }
}