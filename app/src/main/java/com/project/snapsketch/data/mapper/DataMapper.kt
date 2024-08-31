package com.project.snapsketch.data.mapper

import com.project.snapsketch.data.dto.ImageDto
import com.project.snapsketch.domain.entity.ImageEntity

object DataMapper {
    fun ImageDto.toEntity() = ImageEntity(
        uriString = uriString,
        date = date,
        location = location,
    )

    fun ImageEntity.toDto() = ImageDto(
        uriString = uriString,
        date = date,
        location = location,
    )
}