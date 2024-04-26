package com.example.unimarket.ui.Login.ui

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.unimarket.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


@Composable
fun UserInfoScreen(viewModel: UserInfoViewModel,navController: NavHostController) {
    val carreraState: String by viewModel.carrera.observeAsState(initial = "")
    val semestreState: String by viewModel.semestre.observeAsState(initial = "")
    val nombreState: String by viewModel.nombre.observeAsState(initial = "")
    val coroutineScope= rememberCoroutineScope()

    val context = LocalContext.current

    var toast: Toast? by remember { mutableStateOf(null) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HeaderImage()
        Spacer(modifier = Modifier.padding(16.dp))
        Welcome()
        Spacer(modifier = Modifier.padding(8.dp))
        NombreText()
        NombreSpace(nombreState) { viewModel.onInfoChanged(it, carreraState, semestreState) }
        Spacer(modifier = Modifier.padding(16.dp))
        CarreraText()
        CarreraSpace(carreraState) { viewModel.onInfoChanged(nombreState, it, semestreState) }
        Spacer(modifier = Modifier.padding(16.dp))
        SemestreText()
        SemestreSpace(semestreState) { viewModel.onInfoChanged(nombreState, carreraState, it) }
        Spacer(modifier = Modifier.padding(16.dp))
        ButtonGuardar {
            if (viewModel.isNetworkAvailable(context)) {
                coroutineScope.launch(Dispatchers.IO) {
                    viewModel.guardarDatosUsuario()
                }
                navController.navigate("HOME") {
                    popUpTo(route = "LOGIN") { inclusive = true }
                }
            } else {
                toast?.cancel()
                toast = Toast.makeText(context, "No hay conectividad a internet", Toast.LENGTH_SHORT)
                toast?.show()
                Log.d(null, "No hay conectividad a internet")
            }
        }

    }
}


@Composable
fun HeaderImage() {
    Image(
        painter = painterResource(id = R.drawable.logo),
        contentDescription = "Header"
    )
}


@Composable
fun Welcome() {
    Text(
        text = "Bienvenido",
        fontSize = 25.sp,
        color = Color(0xFF181316)
    )
    Text(
        text = "Por favor ingresa tu información",
        fontSize = 15.sp,
        color = Color(0xFF989EB1)
    )
}

@Composable
fun NombreText() {
    Text(
        text = "Nombre",
        fontSize = 17.sp,
        color = Color(0xFFFF5958)
    )
}

@Composable
fun CarreraText() {
    Text(
        text = "Carrera",
        fontSize = 17.sp,
        color = Color(0xFFFF5958)
    )
}

@Composable
fun SemestreText() {
    Text(
        text = "Semestre",
        fontSize = 17.sp,
        color = Color(0xFFFF5958)
    )
}

@Composable
fun NombreSpace(nombreState: String, onNombreChanged: (String) -> Unit) {
    TextField(
        value = nombreState,
        onValueChange = onNombreChanged,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Nombre", color = Color(0xFF989EB1)) },
        singleLine = true
    )
}

@Composable
fun CarreraSpace(carreraState: String, onCarreraChanged: (String) -> Unit) {
    TextField(
        value = carreraState,
        onValueChange = onCarreraChanged,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Carrera", color = Color(0xFF989EB1)) },
        singleLine = true
    )
}

@Composable
fun SemestreSpace(semestreState: String, onSemestreChanged: (String) -> Unit) {
    TextField(
        value = semestreState,
        onValueChange = onSemestreChanged,
        modifier = Modifier.fillMaxWidth(),
        placeholder = { Text(text = "Semestre", color = Color(0xFF989EB1)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        singleLine = true
    )
}

@Composable
fun ButtonGuardar(onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        enabled = true,
        colors = androidx.compose.material.ButtonDefaults.buttonColors(
            backgroundColor = Color(0xFFFF5958)
        )
    ) {
        Text(
            text = "Guardar",
            fontSize = 15.sp,
            color = Color(0xFFFFFFFF)
        )
    }
}
