package com.project.snapsketch.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import com.project.snapsketch.domain.repository.OpencvRepository
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import java.io.ByteArrayOutputStream

class OpencvRepositoryImpl : OpencvRepository{
    override fun getDetectedImage(inputImage: ByteArray, th1: Double, th2: Double): ByteArray {
        val bitmap = BitmapFactory.decodeByteArray(inputImage, 0, inputImage.size)

        val srcMat = Mat()
        Utils.bitmapToMat(bitmap, srcMat)

        val grayMat = Mat()
        Imgproc.cvtColor(srcMat, grayMat, Imgproc.COLOR_BGR2GRAY)

        val edges = Mat()
        Imgproc.Canny(grayMat, edges, th1, th2)

        val invertedEdges = Mat()
        Core.bitwise_not(edges, invertedEdges)

        val resultBitmap = Bitmap.createBitmap(invertedEdges.cols(), invertedEdges.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(invertedEdges, resultBitmap)

        srcMat.release()
        grayMat.release()
        edges.release()
        invertedEdges.release()

        val outputStream = ByteArrayOutputStream()
        resultBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

        return outputStream.toByteArray()
    }
}