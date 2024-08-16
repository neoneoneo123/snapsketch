package com.project.snapsketch.presentation.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Environment
import com.project.snapsketch.presentation.model.ImageModel
import org.opencv.android.Utils
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream

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

    fun convertImage(context: Context, uri: Uri, th1: Double, th2: Double): Bitmap {
        val bitmap = uriToBitmap(context, uri)
        val edgeBitmap = processEdgeDetection(bitmap, th1, th2)
        return edgeBitmap
    }

    fun saveImageToDirectory(context: Context, bitmap: Bitmap): File? {
        val imageName = System.currentTimeMillis().toString()
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

    fun uriToBitmap(context: Context, uri: Uri): Bitmap {
        val inputStream: InputStream? = context.contentResolver.openInputStream(uri)
        return BitmapFactory.decodeStream(inputStream)
    }

    private fun processEdgeDetection(bitmap: Bitmap, th1: Double, th2: Double): Bitmap {
        val srcMat = Mat()
        Utils.bitmapToMat(bitmap, srcMat)

        val grayMat = Mat()
        Imgproc.cvtColor(srcMat, grayMat, Imgproc.COLOR_BGR2GRAY)

        val edges = Mat()
        Imgproc.Canny(grayMat, edges, th1, th2)

        val resultBitmap = Bitmap.createBitmap(edges.cols(), edges.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(edges, resultBitmap)

        srcMat.release()
        grayMat.release()
        edges.release()

        return resultBitmap
    }
}