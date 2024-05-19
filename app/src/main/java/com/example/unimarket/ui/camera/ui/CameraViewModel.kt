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
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch

import android.provider.MediaStore
import android.util.Log
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.firebase.Firebase
import com.google.firebase.storage.storage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

import java.io.IOException
import java.util.UUID


class CameraViewModel : ViewModel() {
    private val _imageUri = MutableLiveData<Uri?>()
    val imageUri: MutableLiveData<Uri?> = _imageUri

    private val _imageFirestoreURL = MutableStateFlow<String>("")
    val imageFirestoreURL : StateFlow<String> = _imageFirestoreURL

    fun captureImage(context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val imageUri = takePicture(context)
                _imageUri.postValue(imageUri)
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }

    @Throws(IOException::class)
    private suspend fun takePicture(context: Context): Uri? {
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
            return imageUri
        }
        return null
    }

    fun uploadImageToFirebase(uri: Uri){
        val storageRef = Firebase.storage.reference
        val imagesRef = storageRef.child("images/${UUID.randomUUID()}")

        imagesRef.putFile(uri)
            .addOnSuccessListener { taskSnapshot ->
                // La imagen se subió exitosamente
                Log.d("FirebaseStorage", "Imagen subida exitosamente: ${taskSnapshot.metadata?.path}")
                //_imageFirestoreURL.value = taskSnapshot.metadata?.path.toString()
                getImageUrl(taskSnapshot.metadata?.path.toString())

            }
            .addOnFailureListener { exception ->
                // Ocurrió un error al subir la imagen
                Log.e("FirebaseStorage", "Error al subir la imagen: $exception")
            }
    }


    private fun getImageUrl(url: String) {
        val storageRef = Firebase.storage.reference

        storageRef.child(url).downloadUrl.addOnSuccessListener {
            Log.d("CameraViewModel",it.toString())
            _imageFirestoreURL.value = it.toString()
        }
    }



    companion object {
        const val REQUEST_IMAGE_CAPTURE = 1
    }
}
