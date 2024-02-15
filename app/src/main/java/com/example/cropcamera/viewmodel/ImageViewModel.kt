package com.example.cropcamera.viewmodel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.cropcamera.repository.ImageRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * ViewModel responsible for handling image-related operations and interactions.
 */
/**
 * Constructs a new ImageViewModel with the provided ImageRepository dependency.
 *
 * @param imageRepository The repository for accessing and managing image data.
 */
@HiltViewModel
class ImageViewModel @Inject constructor(private val imageRepository: ImageRepository) :
    ViewModel() {

    /**
     * Saves the provided bitmap image to the device storage with the given filename.
     *
     * @param bitmap   The bitmap image to be saved.
     * @param filename The filename for the saved image.
     */
    fun saveImage(bitmap: Bitmap, filename: String) {
        imageRepository.saveImage(bitmap, filename)
    }


    /**
     * Retrieves the path of the image from the repository using the provided image path.
     *
     * @param path The path of the image to retrieve.
     * @return A LiveData object containing the path of the image.
     */

    fun imagePath(path: String): LiveData<String?> {
        return imageRepository.readImage(path)
    }
}