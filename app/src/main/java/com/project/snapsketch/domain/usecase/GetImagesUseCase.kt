package com.project.snapsketch.domain.usecase

import com.project.snapsketch.domain.entity.ImageEntity
import com.project.snapsketch.domain.repository.LocalRepository
import javax.inject.Inject

class GetImagesUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke(): List<ImageEntity>? {
        return localRepository.getImages()
    }
}