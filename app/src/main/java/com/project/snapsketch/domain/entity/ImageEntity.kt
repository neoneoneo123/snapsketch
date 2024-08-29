package com.project.snapsketch.domain.entity

import android.net.Uri
import java.util.Date

data class ImageEntity(
    val uri: Uri?,
    val date: Date?,
    val location: String?,
)
