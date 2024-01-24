package com.github.guibrisson.data.di

import com.github.guibrisson.data.repository.RoadmapRepository
import com.github.guibrisson.data.repository.RoadmapRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindsRoadmapRepository(
        roadmapRepositoryImpl: RoadmapRepositoryImpl
    ): RoadmapRepository
}
