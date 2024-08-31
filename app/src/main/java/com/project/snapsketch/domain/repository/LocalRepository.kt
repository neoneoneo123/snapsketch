package com.project.snapsketch.domain.repository

import com.project.snapsketch.domain.entity.ImageEntity

interface LocalRepository {
    suspend fun getImages(): List<ImageEntity>?
    suspend fun deleteImages(uriString: String)
    suspend fun saveImage(inputImage: ByteArray)
}