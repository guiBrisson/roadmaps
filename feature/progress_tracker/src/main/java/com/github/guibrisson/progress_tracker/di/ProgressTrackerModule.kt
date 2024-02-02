package com.github.guibrisson.progress_tracker.di

import android.content.Context
import com.github.guibrisson.progress_tracker.repository.TrackerRepository
import com.github.guibrisson.progress_tracker.repository.TrackerRepositoryImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object ProgressTrackerModule {

    @Provides
    fun providesTrackerRepository(
        @ApplicationContext context: Context,
    ): TrackerRepository = TrackerRepositoryImpl(context)
}
