package com.example.cropcamera.repository

import android.content.Context
import android.graphics.Bitmap
import android.os.Environment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import javax.inject.Inject

/**
 * Implementation of the ImageRepository interface for handling image-related operations.
 * This class is responsible for saving and reading images to and from local storage.
 */
/**
 * Constructs a new instance of the ImageRepositoryImpl class.
 *
 * @param context The application context.
 */
class ImageRepositoryImpl @Inject constructor(context: Context) : ImageRepository {

    /**
     * Saves the given bitmap image to local storage with the specified filename.
     *
     * @param bitmap   The bitmap image to be saved.
     * @param filename The filename to save the image with.
     */
    override fun saveImage(bitmap: Bitmap, filename: String) {
        val directory = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "CropCamera"
        )
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val file = File(directory, filename)
        try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.flush()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    /**
     * Reads an image from the specified path and provides a LiveData object containing the image path.
     *
     * @param imagePath The path of the image to be read.
     * @return A LiveData object containing the path of the read image.
     */
    override fun readImage(imagePath: String): LiveData<String?> {
        val directory = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
            "CropCamera"
        )
        if (!directory.exists()) {
            directory.mkdirs()
        }
        val imagePathLiveData = MutableLiveData<String?>()

        imagePathLiveData.value = "$directory/${imagePath}"

        return imagePathLiveData
    }

}