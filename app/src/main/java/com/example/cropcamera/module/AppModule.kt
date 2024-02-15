package com.example.cropcamera.module

import android.content.Context
import com.example.cropcamera.repository.ImageRepository
import com.example.cropcamera.repository.ImageRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
/**
 * Dagger module for providing dependencies related to the application context and repositories.
 * This module is installed in the Singleton component for application-wide scope.
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    /**
     * Provides the application context.
     *
     * @param context The application context.
     * @return The provided application context.
     */
    @Provides
    @Singleton
    fun provideContext(@ApplicationContext context: Context): Context {
        return context
    }

    /**
     * Provides an instance of the ImageRepository implementation.
     *
     * @param context The application context.
     * @return An instance of the ImageRepository implementation.
     */
    @Provides @Singleton fun provideImageRepository(context : Context) : ImageRepository {
        return ImageRepositoryImpl(context)
    }
}