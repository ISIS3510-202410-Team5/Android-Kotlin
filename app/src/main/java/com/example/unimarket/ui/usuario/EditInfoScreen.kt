package com.example.unimarket.ui.usuario

import androidx.compose.runtime.livedata.observeAsState
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
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
        Button(
            onClick = {
                val nuevosDatos = mapOf(
                    "nombre" to nombre,
                    "carrera" to carrera,
                    "semestre" to semestre
                )
                onSave(nuevosDatos)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5958))
        ) {
            Text("Guardar", color = Color.White)
        }
        Spacer(modifier = Modifier.height(8.dp))
        Button(
            onClick = { onCancel() },
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF5958))
        ) {
            Text("Cancelar", color = Color.White)
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
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "Editar Información del Usuario",
                            fontSize = 24.sp,
                            color = Color(0xFFFF5958),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Actualiza la información del usuario y guarda los cambios.",
                            fontSize = 16.sp,
                            color = Color(0xFF181316),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(24.dp))
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
    }
}