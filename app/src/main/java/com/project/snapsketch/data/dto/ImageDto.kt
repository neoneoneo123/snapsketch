package com.project.snapsketch.data.dto

import android.net.Uri
import java.util.Date

data class ImageDto(
    val uri: Uri?,
    val date: Date?,
    val location: String?,
)
