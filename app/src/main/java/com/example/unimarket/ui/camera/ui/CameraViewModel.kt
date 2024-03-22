package com.example.unimarket.ui.camera.ui

import android.app.Activity
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.graphics.Camera
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraManager
import android.hardware.camera2.CameraMetadata
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

import android.provider.MediaStore
import androidx.core.content.ContextCompat.getSystemService
import kotlinx.coroutines.Dispatchers

import java.io.IOException



class CameraViewModel : ViewModel() {
    fun captureImage(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Capturar la imagen utilizando la cámara del dispositivo
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                val result = intent.resolveActivity(context.packageManager)
                if (result != null) {
                    val imageUri = context.contentResolver.insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        ContentValues()
                    )
                    imageUri?.let {
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, it)
                        val activity = context as Activity
                        activity.startActivityForResult(intent, REQUEST_IMAGE_CAPTURE)
                    }
                }
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }



    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }
}