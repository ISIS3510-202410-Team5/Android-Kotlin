package com.example.unimarket.ui.Login.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.unimarket.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Composable
fun PasswordResetScreen(navController: NavHostController, viewModel: PasswordRecoverViewModel) {
    val context = LocalContext.current
    var email by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()

    var toast: Toast? by remember { mutableStateOf(null) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            text = "Recuperar Contraseña",
            fontSize = 24.sp,
            color = Color(0xFF181316)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Ingrese su correo electrónico para recibir un enlace de recuperación de contraseña.",
            fontSize = 16.sp,
            color = Color(0xFF989EB1)
        )
        Spacer(modifier = Modifier.height(32.dp))
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Correo Electrónico",color= Color(0xFFFF5958)) },
            placeholder = { Text("Ingrese su correo electrónico", color = Color(0xFF989EB1)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.textFieldColors(
                textColor = Color.Black,
                placeholderColor = Color.Black,
                focusedLabelColor = Color.Black,
                unfocusedLabelColor = Color.Black,
                focusedIndicatorColor = Color.Black,
                unfocusedIndicatorColor = Color.Black
            )
        )



        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                coroutineScope.launch {
                    if (viewModel.isNetworkAvailable(context)) {
                        try {
                            val correoExiste = viewModel.verificarCorreoExistente(email)
                            if (correoExiste) {
                                viewModel.resetPassword(email)
                                Toast.makeText(context, "Correo de recuperación enviado", Toast.LENGTH_SHORT).show()
                                navController.popBackStack()
                            } else {
                                toast?.cancel()
                                toast = Toast.makeText(context, "El correo electrónico no está registrado", Toast.LENGTH_SHORT)
                                toast?.show()
                            }
                        } catch (e: Exception) {
                            Log.d(null, "error en el recovery")
                        }
                    } else {
                        toast?.cancel()
                        toast = Toast.makeText(context, "No hay conectividad a internet", Toast.LENGTH_SHORT)
                        toast?.show()
                    }
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color(0xFFFF5958),
                contentColor = Color.White
            )
        ) {
            Text("Enviar", fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))
        SignText(navController)


    }
}
@Composable
fun SignText(navController: NavHostController) {
    { TODO("Aqui toca colocar la parte de navegacion A Sign IN Screen") }
    androidx.compose.material3.Text(
        text = "Sign In",
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentSize(Alignment.Center)
            .clickable {
                Log.d(null, "Se presiona boton de Sign In")
                navController.navigate(route = "LOGIN")
            },
        fontSize = 15.sp,
        color = Color(0xFFFF5958),
        fontFamily = FontFamily.SansSerif

    )
}

