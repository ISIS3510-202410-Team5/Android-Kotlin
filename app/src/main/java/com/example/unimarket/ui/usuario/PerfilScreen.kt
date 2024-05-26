package com.example.unimarket.ui.usuario

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.unimarket.R
import com.example.unimarket.model.UsuarioDTO
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.rememberImagePainter


@Composable
fun UsuarioInfo(usuario: UsuarioDTO) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = 8.dp,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "Tu nombre es:",
                fontSize = 14.sp,
                color = Color.Gray,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = usuario.nombre,
                fontSize = 16.sp,
                color = Color.Black,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Divider(color = Color.LightGray, thickness = 1.dp)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tu carrera es:",
                fontSize = 14.sp,
                color = Color.Gray,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = usuario.carrera,
                fontSize = 16.sp,
                color = Color.Black,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(bottom = 8.dp)
            )
            Divider(color = Color.LightGray, thickness = 1.dp)

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Tu semestre es:",
                fontSize = 14.sp,
                color = Color.Gray,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier.padding(bottom = 4.dp)
            )
            Text(
                text = usuario.semestre,
                fontSize = 16.sp,
                color = Color.Black,
                fontFamily = FontFamily.SansSerif
            )
        }
    }
}

@Composable
fun UsuarioScreen(viewModel: PerfilViewModel = hiltViewModel(), navController: NavHostController) {
    val auth: FirebaseAuth = Firebase.auth
    val currentUser = auth.currentUser
    val context = LocalContext.current
    var toast: Toast? = null

    if (currentUser != null) {
        val correo = currentUser.email
        if (correo != null) {
            val usuarioState by viewModel.obtenerUsuarioPorCorreo(correo).observeAsState()

            if (usuarioState == null) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Text(
                            text = "Do You Have Internet Connection?\nPlease refresh the screen",
                            color = Color(0xFFFF5958),
                            modifier = Modifier.padding(top = 8.dp)
                        )
                    }
                }
            }
            else {
                val isNetworkAvailable = viewModel.isNetworkAvailable(LocalContext.current)

                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color(0xFFF0F0F0)),
                    contentAlignment = Alignment.TopCenter
                ) {
                    usuarioState?.let { usuario ->
                        val context = LocalContext.current // Obtenemos el contexto local

                        Column(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = "Correo: ${usuario.correo}",
                                fontSize = 14.sp,
                                color = Color.Gray,
                                fontFamily = FontFamily.SansSerif,
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                            ProfileImage(imageUrl = usuario.profileImageUrl)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    if (viewModel.isNetworkAvailable(context)) {
                                        try {
                                            navController.navigate("EDITIMG")
                                        } catch (e: Exception) {
                                            toast?.cancel()
                                            toast = Toast.makeText(context, "Error al guardar la imagen", Toast.LENGTH_SHORT)
                                            toast?.show()
                                        }

                                    } else {
                                        toast?.cancel()
                                        toast = Toast.makeText(context, "No hay conectividad a internet", Toast.LENGTH_SHORT)
                                        toast?.show()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5958))
                            ) {
                                Text("Editar Imagen")
                            }

                            Spacer(modifier = Modifier.height(16.dp))
                            UsuarioInfo(usuario)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(
                                onClick = {
                                    if (viewModel.isNetworkAvailable(context)) {
                                        try {
                                            navController.navigate("EDIT")
                                        } catch (e: Exception) {
                                            toast?.cancel()
                                            toast = Toast.makeText(context, "Error al guardar la imagen", Toast.LENGTH_SHORT)
                                            toast?.show()
                                        }

                                    } else {
                                        toast?.cancel()
                                        toast = Toast.makeText(context, "No hay conectividad a internet", Toast.LENGTH_SHORT)
                                        toast?.show()
                                    }
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5958))
                            ) {
                                Text("Editar Informacion")
                            }
                        }
                    } ?: run {
                        CircularProgressIndicator()
                    }
                }
            }
        } else {
            // El correo electrónico del usuario es nulo no debería pasar en ningún caso
        }
    } else {
        // No hay usuario autenticado no debería pasar en ningún caso
    }
}

@Composable
fun ProfileImage(imageUrl: String?) {
    if (imageUrl.isNullOrEmpty()) {
        HeaderImage()
    } else {
        Image(
            painter = rememberImagePainter(imageUrl),
            contentDescription = "User Profile Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape)
                .background(Color.Gray)
        )
    }
}

@Composable
fun HeaderImage() {
    Image(
        painter = painterResource(id = R.drawable.fotousuario),
        contentDescription = "User Profile Image",
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(150.dp)
            .clip(CircleShape)
            .background(Color.Gray)
    )
}
