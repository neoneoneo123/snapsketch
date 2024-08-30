package com.project.snapsketch.presentation.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import java.io.ByteArrayOutputStream

object ImageConverter {

    fun Uri.toBytes(context: Context): ByteArray? {
        return try {
            val inputStream = context.contentResolver.openInputStream(this)
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val outputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, ByteArrayOutputStream())
            outputStream.toByteArray()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun ByteArray.toBitmap(): Bitmap? {
        return try {
            BitmapFactory.decodeByteArray(this, 0, this.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}