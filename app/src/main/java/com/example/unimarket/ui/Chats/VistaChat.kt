package com.example.unimarket.ui.Chats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun VistaDelChat(chatId: String, chatViewModel: ChatViewModel) {

    val mensajes by chatViewModel.mensajes.collectAsState()

    LaunchedEffect(chatId) {
        chatViewModel.obtenerMensajes(chatId)
    }

    Column {
        LazyColumn {
            items(mensajes) { mensaje ->
                Text(text = "${mensaje.remitente}: ${mensaje.contenido}")
            }
        }
        var nuevoMensaje by remember { mutableStateOf("") }
        TextField(
            value = nuevoMensaje,
            onValueChange = { nuevoMensaje = it },
            placeholder = { Text(text = "Escribe un mensaje") }
        )
        Button(onClick = {
            chatViewModel.enviarMensaje(chatId, nuevoMensaje, "cliente@example.com")
            nuevoMensaje = ""
        }) {
            Text(text = "Enviar")
        }
    }
}