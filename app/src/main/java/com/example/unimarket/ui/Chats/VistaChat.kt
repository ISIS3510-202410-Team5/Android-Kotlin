package com.example.unimarket.ui.Chats

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unimarket.ui.usuario.UsuarioViewModel

@Composable
fun VistaDelChat(chatId: String, chatViewModel: ChatViewModel, userViewModel: UsuarioViewModel) {
    val mensajes by chatViewModel.mensajes.collectAsState()
    val chatDetails by chatViewModel.chatDetails.collectAsState()
    val usuarioActual: String = userViewModel.getCorreoUsuarioApp()

    LaunchedEffect(chatId) {
        chatViewModel.obtenerMensajes(chatId)
        chatViewModel.obtenerChatDetails(chatId)
    }

    Log.d("",usuarioActual)

    Column(modifier = Modifier.fillMaxSize()) {
        if (chatDetails != null) {
            Text(text = "Chat entre: ${chatDetails?.emailCliente} y ${chatDetails?.emailProveedor}")
        }

        LazyColumn(
            modifier = Modifier.weight(1f)
        ) {
            items(mensajes) { mensaje ->
                MessageBubble(
                    text = mensaje.contenido,
                    isCurrentUser = mensaje.remitente == usuarioActual
                )
            }
        }

        var nuevoMensaje by remember { mutableStateOf("") }
        TextField(
            value = nuevoMensaje,
            onValueChange = { nuevoMensaje = it },
            placeholder = { Text(text = "Escribe un mensaje") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Button(onClick = {
            val remitente = usuarioActual
            chatViewModel.enviarMensaje(chatId, nuevoMensaje, remitente)
            nuevoMensaje = ""
        }, modifier = Modifier.align(Alignment.End).padding(8.dp)) {
            Text(text = "Enviar")
        }
    }
}

@Composable
fun MessageBubble(
    text: String,
    isCurrentUser: Boolean
) {
    val backgroundColor = if (isCurrentUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
    val alignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
    val bubbleShape = if (isCurrentUser) RoundedCornerShape(12.dp, 12.dp, 0.dp, 12.dp) else RoundedCornerShape(12.dp, 12.dp, 12.dp, 0.dp)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        contentAlignment = alignment
    ) {
        Surface(
            color = backgroundColor,
            shape = bubbleShape,
            modifier = Modifier
                .widthIn(max = 250.dp)
        ) {
            Text(
                text = text,
                modifier = Modifier.padding(8.dp),
                color = Color.White,
                fontSize = 16.sp,
                textAlign = if (isCurrentUser) TextAlign.End else TextAlign.Start
            )
        }
    }
}