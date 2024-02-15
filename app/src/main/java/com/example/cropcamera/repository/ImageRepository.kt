package com.example.cropcamera.repository

import android.graphics.Bitmap
import androidx.lifecycle.LiveData

/**
 * Interface for handling image-related operations such as saving and reading images.
 */
interface ImageRepository {
    /**
     * Saves the given bitmap image to local storage with the specified filename.
     *
     * @param bitmap   The bitmap image to be saved.
     * @param filename The filename to save the image with.
     */
    fun saveImage(bitmap: Bitmap, filename: String)

    /**
     * Reads an image from the specified path and provides a LiveData object containing the image path.
     *
     * @param path The path of the image to be read.
     * @return A LiveData object containing the path of the read image.
     */
    fun readImage(path: String): LiveData<String?>
}