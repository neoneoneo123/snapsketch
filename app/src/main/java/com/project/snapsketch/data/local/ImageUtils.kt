package com.project.snapsketch.data.local

import android.content.Context
import android.location.Geocoder
import android.net.Uri
import androidx.exifinterface.media.ExifInterface
import java.text.SimpleDateFormat
import java.util.Locale

object ImageUtils {
    fun getImageMetadata(context: Context, uri: Uri): Pair<String?, String?>? {
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

                val latitude =
                    exif.getAttribute(ExifInterface.TAG_GPS_LATITUDE)?.let { convertToDegree(it) }
                val longitude =
                    exif.getAttribute(ExifInterface.TAG_GPS_LONGITUDE)?.let { convertToDegree(it) }
                val location = if (latitude != null && longitude != null) {
                    getAddressFromCoordinates(context, latitude, longitude)
                } else {
                    null
                }

                return Pair(date, location)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    private fun getAddressFromCoordinates(
        context: Context,
        latitude: Double,
        longitude: Double
    ): String? {
        return try {
            val geocoder = Geocoder(context, Locale.getDefault())
            val addresses = geocoder.getFromLocation(latitude, longitude, 1)
            addresses?.firstOrNull()?.locality
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun convertToDegree(stringDMS: String): Double {
        val dms = stringDMS.split(",", limit = 3)

        val degrees = dms[0].split("/").let { it[0].toDouble() / it[1].toDouble() }
        val minutes = dms[1].split("/").let { it[0].toDouble() / it[1].toDouble() }
        val seconds = dms[2].split("/").let { it[0].toDouble() / it[1].toDouble() }

        return degrees + (minutes / 60) + (seconds / 3600)
    }
}