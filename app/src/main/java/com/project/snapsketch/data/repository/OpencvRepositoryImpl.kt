package com.project.snapsketch.data.repository

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import com.project.snapsketch.domain.repository.OpencvRepository
import org.opencv.android.Utils
import org.opencv.core.Core
import org.opencv.core.Mat
import org.opencv.imgproc.Imgproc
import java.io.ByteArrayOutputStream
import javax.inject.Inject

class OpencvRepositoryImpl @Inject constructor() : OpencvRepository{
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

        val rotatedMat = Mat()
        Core.rotate(invertedEdges, rotatedMat, Core.ROTATE_90_CLOCKWISE)

        val resultBitmap = Bitmap.createBitmap(rotatedMat.cols(), rotatedMat.rows(), Bitmap.Config.ARGB_8888)
        Utils.matToBitmap(rotatedMat, resultBitmap)

        srcMat.release()
        grayMat.release()
        edges.release()
        invertedEdges.release()
        rotatedMat.release()

        val outputStream = ByteArrayOutputStream()
        resultBitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)

        return outputStream.toByteArray()
    }
}