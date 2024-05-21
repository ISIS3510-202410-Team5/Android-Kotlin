package com.example.unimarket.ui.usuario

import androidx.compose.foundation.Image
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


@Composable
fun UsuarioInfo(usuario: UsuarioDTO) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Nombre: ${usuario.nombre}")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Carrera: ${usuario.carrera}")
        Spacer(modifier = Modifier.height(8.dp))
        Text(text = "Semestre: ${usuario.semestre}")
    }
}

@Composable
fun UsuarioScreen(viewModel: PerfilViewModel = hiltViewModel(), navController: NavHostController) {
    val auth: FirebaseAuth = Firebase.auth
    val currentUser = auth.currentUser

    var modoEdicion by remember { mutableStateOf(false) }

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
                            HeaderImage()
                            Spacer(modifier = Modifier.height(16.dp))
                            UsuarioInfo(usuario)
                            Spacer(modifier = Modifier.height(16.dp))
                            Button(onClick = {
                                navController.navigate("EDIT")
                            }) {
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
fun HeaderImage() {
    Image(
        painter = painterResource(id = R.drawable.fotousuario),
        contentDescription = "User Profile Image",
        modifier = Modifier.size(150.dp)
    )
}
