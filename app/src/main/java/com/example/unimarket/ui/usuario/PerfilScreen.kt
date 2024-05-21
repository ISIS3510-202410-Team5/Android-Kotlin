package com.example.unimarket.ui.usuario

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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

@Composable
fun UsuarioInfo(usuario: UsuarioDTO) {
    Column(modifier = Modifier.padding(16.dp)) {
        Text(text = "Nombre: ${usuario.nombre}")
        Text(text = "Carrera: ${usuario.carrera}")
        Text(text = "Semestre: ${usuario.semestre}")
    }
}

@Composable
fun UsuarioScreen(viewModel: PerfilViewModel = hiltViewModel(), navController: NavHostController) {
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
                            HeaderImage()
                            Spacer(modifier = Modifier.height(16.dp))
                            UsuarioInfo(usuario)
                        }
                    } ?: run {
                        CircularProgressIndicator()
                    }
                }
            }
        } else {
            // El correo electrónico del usuario es nulo no deberia pasar en ningun caso
        }
    } else {
        // No hay usuario autenticado no deberia pasar en ningun cas
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
