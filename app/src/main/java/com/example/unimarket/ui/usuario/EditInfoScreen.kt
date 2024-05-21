package com.example.unimarket.ui.usuario

import androidx.compose.runtime.livedata.observeAsState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.unimarket.model.UsuarioDTO

@Composable
fun EditarUsuarioForm(
    usuario: UsuarioDTO,
    onSave: (Map<String, Any>) -> Unit,
    onCancel: () -> Unit
) {
    var nombre by remember { mutableStateOf(usuario.nombre) }
    var carrera by remember { mutableStateOf(usuario.carrera) }
    var semestre by remember { mutableStateOf(usuario.semestre) }

    Column(modifier = Modifier.padding(16.dp)) {
        TextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = { Text("Nombre") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = carrera,
            onValueChange = { carrera = it },
            label = { Text("Carrera") }
        )
        Spacer(modifier = Modifier.height(8.dp))
        TextField(
            value = semestre,
            onValueChange = { semestre = it },
            label = { Text("Semestre") }
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = {
            val nuevosDatos = mapOf(
                "nombre" to nombre,
                "carrera" to carrera,
                "semestre" to semestre
            )
            onSave(nuevosDatos)
        }) {
            Text("Guardar")
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onCancel() }) {
            Text("Cancelar")
        }
    }
}

@Composable
fun EditarUsuarioScreen(
    viewModel: PerfilViewModel = hiltViewModel(),
    navController: NavHostController
) {
    val auth: FirebaseAuth = Firebase.auth
    val currentUser = auth.currentUser

    if (currentUser != null) {
        val correo = currentUser.email
        if (correo != null) {
            val usuarioState by viewModel.obtenerUsuarioPorCorreo(correo).observeAsState()

            usuarioState?.let { usuario ->
                EditarUsuarioForm(
                    usuario = usuario,
                    onSave = { nuevosDatos ->
                        viewModel.actualizarUsuario(correo, nuevosDatos)
                        navController.popBackStack()
                    },
                    onCancel = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
