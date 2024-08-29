package com.project.snapsketch.data.mapper

import com.project.snapsketch.data.dto.ImageDto
import com.project.snapsketch.domain.entity.ImageEntity

object DataMapper {
    fun ImageDto.toEntity() = ImageEntity(
        uri = uri,
        date = date,
        location = location,
    )

    fun ImageEntity.toDto() = ImageDto(
        uri = uri,
        date = date,
        location = location,
    )
}