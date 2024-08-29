package com.project.snapsketch.domain.usecase

import com.project.snapsketch.domain.repository.OpencvRepository
import javax.inject.Inject

class GetDetectedImageUseCase @Inject constructor(
    private val opencvRepository: OpencvRepository
) {
    operator fun invoke(inputImage: ByteArray, th1: Double, th2: Double): ByteArray {
        return opencvRepository.getDetectedImage(inputImage, th1, th2)
    }
}