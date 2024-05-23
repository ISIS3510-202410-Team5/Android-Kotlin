package com.example.unimarket.ui.usuario

import android.widget.ImageView
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.compose.rememberImagePainter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.example.unimarket.model.UsuarioDTO
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun CambioImagenPerfil(viewModel: PerfilViewModel = hiltViewModel(), navController: NavHostController) {
    val auth: FirebaseAuth = Firebase.auth
    val currentUser = auth.currentUser

    if (currentUser != null) {
        val correo = currentUser.email
        if (correo != null) {
            val usuarioState by viewModel.obtenerUsuarioPorCorreo(correo).observeAsState()
            var showFullScreenImage by remember { mutableStateOf(false) }

            usuarioState?.let { usuario ->
                var url by remember { mutableStateOf(TextFieldValue(usuario.profileImageUrl ?: "")) }

                if (showFullScreenImage) {

                    ProfileImageFullScreen(url.text, onDismiss = { showFullScreenImage = false })
                } else {
                    // Mostrar la interfaz de cambio de imagen de perfil
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Cambiar Imagen de Perfil",
                            fontSize = 24.sp,
                            color = Color(0xFFFF5958),
                            modifier = Modifier.padding(bottom = 16.dp)
                        )
                        BasicTextField(
                            value = url,
                            onValueChange = { url = it },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            singleLine = true
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        GlideImage(url.text)
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(
                            onClick = {
                                val nuevosDatos = mapOf("profileImageUrl" to url.text)
                                viewModel.actualizarUsuario(correo, nuevosDatos)
                                navController.popBackStack()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5958))
                        ) {
                            Text("Guardar")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = { navController.popBackStack() },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5958))
                        ) {
                            Text("Cancelar")
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(
                            onClick = {
                                CoroutineScope(Dispatchers.Main).launch {
                                    showFullScreenImage = true
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5958))
                        ) {
                            Text("Ver Imagen Completa")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun GlideImage(url: String) {
    val context = LocalContext.current
    AndroidView(
        factory = { ctx ->
            ImageView(ctx).apply {
                Glide.with(ctx)
                    .load(url)
                    .apply(RequestOptions.circleCropTransform())
                    .into(this)
            }
        },
        modifier = Modifier.size(150.dp)
    )
}

@Composable
fun ProfileImageFullScreen(imageUrl: String, onDismiss: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        Image(
            painter = rememberImagePainter(
                data = imageUrl,
                builder = {
                    crossfade(true)
                }
            ),
            contentDescription = "User Profile Image",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .fillMaxSize()
                .clickable { onDismiss() }
        )
    }
}
