package com.example.unimarket.ui.usuario

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
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.unit.sp


@Composable
fun UsuarioInfo(usuario: UsuarioDTO) {
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
            fontFamily = FontFamily.SansSerif
        )
        Spacer(modifier = Modifier.height(16.dp))

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
            fontFamily = FontFamily.SansSerif
        )
        Spacer(modifier = Modifier.height(16.dp))

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

@Composable
fun UsuarioScreen(viewModel: PerfilViewModel, navController: NavHostController) {
    val auth: FirebaseAuth = Firebase.auth
    val currentUser = auth.currentUser

    if (currentUser != null) {
        val correo = currentUser.email
        if (correo != null) {
            val usuarioState by viewModel.obtenerUsuarioPorCorreo(correo).observeAsState()

            if (usuarioState == null) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    usuarioState?.let { usuario ->
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(text = "Correo: ${usuario.correo}")
                            ProfileImage(imageUrl = usuario.profileImageUrl)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Editar imagen",
                                color = Color.Blue,
                                modifier = Modifier.clickable {
                                    navController.navigate("EDITIMG")
                                }
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            UsuarioInfo(usuario)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = {
                                navController.navigate("EDIT")
                            },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5958))) {
                                Text("Editar")
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
        GlideImage(imageUrl
        )
    }
}

@Composable
fun HeaderImage() {
    Image(
        painter = painterResource(id = R.drawable.fotousuario),
        contentDescription = "User Profile Image",
        modifier = Modifier.size(150.dp)
    )
}
