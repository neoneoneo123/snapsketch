package com.project.snapsketch.app.di

import com.project.snapsketch.data.repository.LocalRepositoryImpl
import com.project.snapsketch.data.repository.OpencvRepositoryImpl
import com.project.snapsketch.domain.repository.LocalRepository
import com.project.snapsketch.domain.repository.OpencvRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class RepositoryModule {

    @Provides
    @Singleton
    fun provideLocalRepository(
        localRepositoryImpl: LocalRepositoryImpl
    ): LocalRepository {
        return localRepositoryImpl
    }

    @Provides
    @Singleton
    fun provideOpencvRepository(
        opencvRepositoryImpl: OpencvRepositoryImpl
    ): OpencvRepository {
        return opencvRepositoryImpl
    }
}