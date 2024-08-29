package com.project.snapsketch.data.local

import com.project.snapsketch.data.dto.ImageDto

interface LocalDataSource {

    suspend fun getImages(): List<ImageDto>?
    suspend fun deleteImages(uriString: String)
    suspend fun saveImage(inputImage: ByteArray)
}