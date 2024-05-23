package com.example.unimarket.ui.usuario

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.unimarket.model.UsuarioDTO
import com.example.unimarket.R




@Composable
fun UserProfileScreen(
    navController: NavHostController,usuarioViewModel: UsuarioViewModel, usuario: UsuarioDTO,
    isLoading: Boolean = false
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                HeaderImage()
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = "Bienvenido de nuevo, ${usuario.nombre}!",
                    fontSize = 20.sp,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Center,
                    color = Color(0xFF181316)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Correo: ${usuario.correo}",
                    fontSize = 16.sp,
                    fontFamily = FontFamily.SansSerif,
                    color = Color(0xFF181316)
                )
                Text(
                    text = "Carrera: ${usuario.carrera}",
                    fontSize = 16.sp,
                    fontFamily = FontFamily.SansSerif,
                    color = Color(0xFF181316)
                )
                Text(
                    text = "Semestre: ${usuario.semestre}",
                    fontSize = 16.sp,
                    fontFamily = FontFamily.SansSerif,
                    color = Color(0xFF181316)
                )
            }
        }
    }
}

