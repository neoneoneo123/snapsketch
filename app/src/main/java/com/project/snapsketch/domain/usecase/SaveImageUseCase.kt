package com.project.snapsketch.domain.usecase

import com.project.snapsketch.domain.repository.LocalRepository
import javax.inject.Inject

class SaveImageUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke(inputImage: ByteArray) {
        return localRepository.saveImage(inputImage)
    }
}