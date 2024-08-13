package com.project.snapsketch.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.project.snapsketch.presentation.model.ImageModel
import java.io.File
import java.io.FileOutputStream

object FileUtils {
    private const val DIRECTORY_NAME = "SnapSketch"

    fun getImagesFromDirectory(context: Context): List<ImageModel> {
        val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), DIRECTORY_NAME)
        return if (directory.exists() && directory.isDirectory) {
            directory.listFiles { file -> file.extension == "jpg" }
                ?.map { file -> ImageModel(Uri.fromFile(file)) }
                ?: emptyList()
        } else {
            emptyList()
        }
    }

    fun convertUri(context: Context, uri: Uri): File? {
        val inputStream = context.contentResolver.openInputStream(uri)
        val bitmap = BitmapFactory.decodeStream(inputStream)
        val resizedBitmap = Bitmap.createScaledBitmap(bitmap, 100, 100, true)
        return saveImageToDirectory(context, resizedBitmap, System.currentTimeMillis().toString())
    }

    private fun saveImageToDirectory(context: Context, bitmap: Bitmap, imageName: String): File? {
        val directory = File(context.getExternalFilesDir(Environment.DIRECTORY_PICTURES), DIRECTORY_NAME)
        if (!directory.exists()) {
            directory.mkdirs()
        }

        val file = File(directory, "$imageName.jpg")
        return try {
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream)
            outputStream.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}