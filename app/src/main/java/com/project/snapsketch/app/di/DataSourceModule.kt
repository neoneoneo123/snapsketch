package com.project.snapsketch.app.di

import android.app.Application
import android.content.Context
import com.project.snapsketch.data.local.LocalDataSource
import com.project.snapsketch.data.local.LocalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataSourceModule {

    @Provides
    @Singleton
    fun provideLocalDataSource(
        localDataSourceImpl: LocalDataSourceImpl
    ): LocalDataSource {
        return localDataSourceImpl
    }

    @Provides
    @Singleton
    fun provideApplicationContext(application: Application): Context {
        return application.applicationContext
    }
}