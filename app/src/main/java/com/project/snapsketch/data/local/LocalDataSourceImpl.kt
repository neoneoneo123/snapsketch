package com.project.snapsketch.data.local

import android.content.ContentUris
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import android.provider.MediaStore
import com.project.snapsketch.data.dto.ImageDto
import com.project.snapsketch.data.local.ImageUtils.getImageMetadata
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream
import javax.inject.Inject

class LocalDataSourceImpl @Inject constructor(
    private val context: Context
) : LocalDataSource {

    private val DIRECTORY_NAME = "SnapSketch"

    override suspend fun getImages(): List<ImageDto>? {
        return try {
            val images = mutableListOf<ImageDto>()
            val projection = arrayOf(
                MediaStore.Images.Media._ID,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_TAKEN
            )

            val sortOrder = "${MediaStore.Images.Media.DATE_TAKEN} DESC"

            val cursor = context.contentResolver.query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, sortOrder
            )

            cursor?.use {
                val idColumn = it.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

                while (it.moveToNext()) {
                    val id = it.getLong(idColumn)
                    val contentUri =
                        ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)

                    val metadata = getImageMetadata(context, contentUri)

                    images.add(ImageDto(contentUri.toString(), metadata?.first, metadata?.second))
                }
            }

            images
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    override suspend fun deleteImages(uriString: String) {
        try {
            val uri = Uri.parse(uriString)
            val path = uri.path

            if (path != null) {
                val file = File(path)

                if (file.exists()) {
                    file.delete()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override suspend fun saveImage(inputImage: ByteArray) {
        val bitmap = BitmapFactory.decodeByteArray(inputImage, 0, inputImage.size)
        val imageName = "$DIRECTORY_NAME-${System.currentTimeMillis()}"
        val directory =
            File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), DIRECTORY_NAME)

        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, "$imageName.png")

        try {
            val outputStream = withContext(Dispatchers.IO) {
                FileOutputStream(file)
            }

            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)

            withContext(Dispatchers.IO) {
                outputStream.close()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}