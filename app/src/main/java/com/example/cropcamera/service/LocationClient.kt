package com.example.cropcamera.service

import android.location.Location
import kotlinx.coroutines.flow.Flow

/**
 * Interface for providing location updates.
 */
interface LocationClient {

    /**
     * Retrieves the flow of location updates.
     *
     * @param interval The interval at which location updates should be received.
     * @return A flow of location updates.
     */
    fun getLocationUpdates(interval: Long): Flow<Location>

    /**
     * Constructs a new LocationException with the specified detail message.
     *
     * @param message The detail message.
     */
    class LocationException(message: String): Exception()
}