package com.example.unimarket.ui.Chats

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.unimarket.di.SharedPreferenceService
import com.example.unimarket.model.Chat
import com.example.unimarket.ui.navigation.Screen
import com.example.unimarket.ui.usuario.UsuarioViewModel

@Composable
fun ListaDeChats(chatViewModel: ChatViewModel, navController: NavHostController) {

    SharedPreferenceService.getCurrentUser()?.let { chatViewModel.obtenerChats(it) }

    val chats by chatViewModel.chats.collectAsState(emptyList())

    Column {

        Text(
            text = "Lista de Chats",
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        )

        Spacer(modifier = Modifier.size(14.dp))

        LazyColumn(modifier = Modifier.fillMaxSize()) { // Use fillMaxSize to occupy the available space
            items(chats) { chat ->
                ChatItem( // Create a reusable ChatItem composable
                    chat = chat,
                    onClick = {
                        navController.navigate(Screen.ChatDetail.route + "/${chat.id}")
                    }
                )
            }
        }

    }
}

@Composable
fun ChatItem(
    chat: Chat,
    onClick: () -> Unit,
) {
    Card( // Use Card for a visually appealing background
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable { onClick() }
    ) {
        Row( // Arrange content horizontally
            modifier = Modifier.padding(8.dp)
        ) {

            Column(modifier = Modifier.weight(1f)) { // Expand content within remaining space
                Text(
                    text = "Chat con: ${chat.emailProveedor}",
                    style = MaterialTheme.typography.headlineSmall, // Use a heading style for emphasis
                    maxLines = 1, // Limit title to one line (optional)
                    overflow = TextOverflow.Ellipsis // Ellipsis if text overflows
                )
                if (chat.mensajes.isNotEmpty()) {
                    Text(
                        text = chat.mensajes.last().contenido,
                        style = MaterialTheme.typography.bodyMedium, // Use appropriate body style
                        maxLines = 1, // Limit message preview to one line (optional)
                        overflow = TextOverflow.Ellipsis // Ellipsis if message overflows
                    )
                }
            }
        }
    }
}