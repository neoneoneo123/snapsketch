package com.project.snapsketch.data.repository

import com.project.snapsketch.data.local.LocalDataSource
import com.project.snapsketch.data.mapper.DataMapper.toEntity
import com.project.snapsketch.domain.entity.ImageEntity
import com.project.snapsketch.domain.repository.LocalRepository
import javax.inject.Inject

class LocalRepositoryImpl @Inject constructor (
    private val localDataSource: LocalDataSource
) : LocalRepository {
    override suspend fun getImages(): List<ImageEntity>? {
        return localDataSource.getImages()?.map { it.toEntity() }
    }

    override suspend fun deleteImages(uriString: String) {
        localDataSource.deleteImages(uriString)
    }

    override suspend fun saveImage(inputImage: ByteArray) {
        localDataSource.saveImage(inputImage)
    }
}