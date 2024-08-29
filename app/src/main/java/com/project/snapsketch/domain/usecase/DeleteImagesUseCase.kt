package com.project.snapsketch.domain.usecase

import com.project.snapsketch.domain.repository.LocalRepository
import javax.inject.Inject

class DeleteImagesUseCase @Inject constructor(
    private val localRepository: LocalRepository
) {
    suspend operator fun invoke(uriString: String) {
        localRepository.deleteImages(uriString)
    }
}