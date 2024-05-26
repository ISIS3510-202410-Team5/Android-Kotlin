package com.example.unimarket.ui.Chats

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.unimarket.di.SharedPreferenceService
import com.example.unimarket.ui.navigation.Screen
import com.example.unimarket.ui.usuario.PerfilViewModel
import com.example.unimarket.ui.usuario.UsuarioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VistaDelChat(chatId: String, chatViewModel: ChatViewModel, navController: NavHostController, perfilViewModel: PerfilViewModel) {

    val mensajes by chatViewModel.mensajes.collectAsState()
    val chatDetails by chatViewModel.chatDetails.collectAsState()
    val usuarioActual: String? = SharedPreferenceService.getCurrentUser()

    LaunchedEffect(chatId) {
        chatViewModel.obtenerMensajes(chatId)
        chatViewModel.obtenerChatDetails(chatId)
    }

    val listState = rememberLazyListState()

    LaunchedEffect(mensajes) {
        listState.animateScrollToItem(mensajes.size - 1)
    }

    Column(modifier = Modifier.fillMaxSize()) {
        if (chatDetails != null) {

            TopAppBar(
                navigationIcon = {
                    IconButton(onClick = { navController.navigate("LISTCHATS") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                title = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Button(
                            onClick = { navController.navigate(Screen.InfoChat.route + "/${chatId}") },
                            modifier = Modifier.fillMaxWidth().padding(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Gray),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Info,
                                    contentDescription = "Chat Detail",
                                    modifier = Modifier.size(24.dp),
                                    tint = Color.Black // Color del icono
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    "Chat Information",
                                    style = MaterialTheme.typography.bodyLarge,
                                    color = Color.Black // Color del texto
                                )
                            }
                        }
                    }
                }
            )

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
            if (remitente != null) {
                chatViewModel.enviarMensaje(chatId, nuevoMensaje, remitente)
            }
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
    val backgroundColor =
        if (isCurrentUser) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.secondary
    val alignment = if (isCurrentUser) Alignment.CenterEnd else Alignment.CenterStart
    val bubbleShape = if (isCurrentUser) RoundedCornerShape(
        12.dp,
        12.dp,
        0.dp,
        12.dp
    ) else RoundedCornerShape(12.dp, 12.dp, 12.dp, 0.dp)

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