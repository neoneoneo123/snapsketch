package com.project.snapsketch.data.local

import android.content.Context
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import java.text.SimpleDateFormat
import java.util.Locale

object ImageUtils {
    fun getImageMetadata(context: Context, uri: Uri): String? {
        try {
            context.contentResolver.openInputStream(uri)?.use { inputStream ->
                val exif = ExifInterface(inputStream)

                val dateString = exif.getAttribute(ExifInterface.TAG_DATETIME)
                val date = dateString?.let {
                    val inputFormat = SimpleDateFormat("yyyy:MM:dd HH:mm:ss", Locale.getDefault())
                    val outputFormat = SimpleDateFormat("yyyy/MM/dd", Locale.getDefault())
                    val parsedDate = inputFormat.parse(it)
                    parsedDate?.let { outputFormat.format(it) }
                }

                return date
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }
}