package com.example.unimarket.ui.camera.ui

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.example.unimarket.ui.navigation.Screen
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun CameraScreen(
    viewModel: CameraViewModel,
    lightViewModel: LightSensorViewModel,
    navController: NavHostController
) {
    val context = LocalContext.current
    val lightValue by lightViewModel.lightValue.observeAsState()

    // Estado para almacenar la URI de la imagen seleccionada
    var imageUri by remember { mutableStateOf<Uri?>(null)
    }

    // Lanzador para seleccionar una imagen de la galería
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        imageUri = uri
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))


        imageUri?.let { uri ->
            Image(
                painter = rememberImagePainter(uri),
                contentDescription = null,
                modifier = Modifier
                    .size(400.dp)
                    .padding(16.dp)
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Button(
                onClick = {
                    if (lightValue != null && lightValue!! < 1) {
                        Toast.makeText(
                            context,
                            "The environment is too dark to capture an image",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        viewModel.captureImage(context)
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFFFF5958),
                    contentColor = Color.White
                )
            ) {
                Text("Capture Image")
            }
            
            Button(
                onClick = {
                    launcher.launch("image/*")
                },
                colors = ButtonDefaults.buttonColors(
                    backgroundColor = Color(0xFF42A5F5),
                    contentColor = Color.White
                )
            ) {
                Text("Choose Image")
            }


        }
        /*Button(
            onClick = { imageUri?.let { uri -> viewModel.uploadImageToFirebase(uri) } },
            modifier = Modifier.padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF4CAF50),
                contentColor = Color.White
            )
        ) {
            Text("Save Image")
        }*/
        Button(
            onClick = { imageUri?.let { uri ->
                navigateToAnotherView(navController, imageUri!!) } },
            modifier = Modifier.padding(16.dp),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFF4CAF50),
                contentColor = Color.White
            )
        ) {
            Text("Save Image")
        }



    }

}
fun navigateToAnotherView(navController: NavHostController, uri: Uri) {
    Log.d("CameraScreen", "invoqued navigateToAnotherView")
    Log.d("CameraScreen", "the image uri is: ${uri}")
    navController.navigate("POST?imageUri=${uri}")
}