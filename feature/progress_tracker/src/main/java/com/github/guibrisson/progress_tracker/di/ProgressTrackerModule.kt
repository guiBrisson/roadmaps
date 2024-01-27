package com.github.guibrisson.progress_tracker.di

import com.github.guibrisson.progress_tracker.repository.TrackerRepository
import com.github.guibrisson.progress_tracker.repository.TrackerRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ProgressTrackerModule {

    @Binds
    fun bindsTrackerRepository(
        trackerRepositoryImpl: TrackerRepositoryImpl
    ): TrackerRepository
}
