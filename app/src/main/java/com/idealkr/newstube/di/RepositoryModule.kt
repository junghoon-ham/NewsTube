package com.idealkr.newstube.di

import com.idealkr.newstube.data.repository.VideoRepositoryImpl
import com.idealkr.newstube.domain.repository.VideoRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun bindNewsRepository(
        newsRepositoryImpl: VideoRepositoryImpl,
    ): VideoRepository
}