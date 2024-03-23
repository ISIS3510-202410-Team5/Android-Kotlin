package com.example.unimarket.ui.camera.ui

import android.graphics.Bitmap
import android.widget.Toast
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp

@Composable
fun CameraScreen(
    viewModel: CameraViewModel,
    lightViewModel: LightSensorViewModel
) {
    val context = LocalContext.current
    val capturedImage = remember { mutableStateOf<Bitmap?>(null) }

    val lightValue by lightViewModel.lightValue.observeAsState()




    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (capturedImage.value != null) {
            Image(
                bitmap = capturedImage.value!!.asImageBitmap(),
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                contentScale = ContentScale.FillWidth
            )
        } else {
            Text(text = "No image captured")
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (lightValue != null && lightValue!! < 200) {
                    Toast.makeText(context, "The environment is too dark to capture an image", Toast.LENGTH_SHORT).show()
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
    }
}
