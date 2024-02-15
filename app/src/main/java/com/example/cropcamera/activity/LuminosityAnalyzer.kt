package com.example.cropcamera.activity

import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import java.nio.ByteBuffer

/**
 * Analyzer for calculating the luminosity of images captured by the camera.
 * This analyzer is used with the CameraX ImageAnalysis use case.
 */
class LuminosityAnalyzer(private val listener: LumaListener) : ImageAnalysis.Analyzer {

    /**
     * Analyzes the given image to calculate its luminosity.
     * @param image The ImageProxy representing the captured image.
     */

    override fun analyze(image: ImageProxy) {
        val buffer = image.planes[0].buffer
        val data = buffer.toByteArray()
        val pixels = data.map { it.toInt() and 0xFF }
        val luma = pixels.average().toFloat()
        listener(luma)
        image.close()
    }

    /**
     * Converts a ByteBuffer to a byte array.
     * @return The byte array containing the data from the ByteBuffer.
     */
    private fun ByteBuffer.toByteArray(): ByteArray {
        rewind()
        val data = ByteArray(remaining())
        get(data)
        return data
    }
}

/**
 * Calculates the average luminosity value from an array of pixel values.
 * @param pixels The array of pixel values.
 * @return The average luminosity value.
 */
typealias LumaListener = (luma: Float) -> Unit