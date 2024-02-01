package com.github.guibrisson.data.di

import android.content.Context
import com.github.guibrisson.data.repository.RoadmapRepository
import com.github.guibrisson.data.repository.RoadmapRepositoryImpl
import com.github.guibrisson.progress_tracker.repository.TrackerRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object DataModule {

    @Provides
    fun providesRoadmapRepository(
        @ApplicationContext context: Context,
        trackerRepository: TrackerRepository,
    ): RoadmapRepository = RoadmapRepositoryImpl(context, trackerRepository)
}
