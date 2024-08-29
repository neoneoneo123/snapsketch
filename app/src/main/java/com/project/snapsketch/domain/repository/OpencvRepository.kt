package com.project.snapsketch.domain.repository

interface OpencvRepository {
    fun getDetectedImage(inputImage: ByteArray, th1: Double, th2: Double) : ByteArray
}