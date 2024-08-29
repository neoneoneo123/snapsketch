package com.project.snapsketch.app.di

import com.project.snapsketch.domain.repository.LocalRepository
import com.project.snapsketch.domain.repository.OpencvRepository
import com.project.snapsketch.domain.usecase.DeleteImagesUseCase
import com.project.snapsketch.domain.usecase.GetDetectedImageUseCase
import com.project.snapsketch.domain.usecase.GetImagesUseCase
import com.project.snapsketch.domain.usecase.SaveImageUseCase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped

@Module
@InstallIn(ViewModelComponent::class)
object UseCaseModule {

    @Provides
    @ViewModelScoped
    fun provideDeleteImageUseCase(
        localRepository: LocalRepository
    ): DeleteImagesUseCase {
        return DeleteImagesUseCase(localRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetImagesUseCase(
        localRepository: LocalRepository
    ): GetImagesUseCase {
        return GetImagesUseCase(localRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideSavaImageUseCase(
        localRepository: LocalRepository
    ): SaveImageUseCase {
        return SaveImageUseCase(localRepository)
    }

    @Provides
    @ViewModelScoped
    fun provideGetDetectedImageUseCase(
        opencvRepository: OpencvRepository
    ): GetDetectedImageUseCase {
        return GetDetectedImageUseCase(opencvRepository)
    }
}