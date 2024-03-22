package com.example.unimarket.ui.camera.model

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageFormat
import android.graphics.Rect
import android.graphics.YuvImage
import androidx.camera.core.ImageProxy
import java.io.ByteArrayOutputStream
import java.nio.ByteBuffer

class CameraModel {
    suspend fun processImage(image: ImageProxy): Bitmap {
        return convertImageProxyToBitmap(image)
    }

    private fun convertImageProxyToBitmap(image: ImageProxy): Bitmap {
        val buffer: ByteBuffer = image.planes[0].buffer
        val bytes = ByteArray(buffer.capacity()).apply {
            buffer.get(this)
        }
        val output = ByteArrayOutputStream()
        YuvImage(bytes, ImageFormat.NV21, image.width, image.height, null).compressToJpeg(
            Rect(0, 0, image.width, image.height), 100, output
        )
        val imageBytes: ByteArray = output.toByteArray()
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.size)
    }
}