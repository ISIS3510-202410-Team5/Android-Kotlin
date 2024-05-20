package com.example.unimarket.ui.Chats

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
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

    LazyColumn {
        items(chats) { chat ->
            Column(modifier = Modifier.clickable {
                navController.navigate(Screen.ChatDetail.route + "/${chat.id}")
            }) {
                Text(text = "Chat con: ${chat.emailProveedor}")
                if (chat.mensajes.isNotEmpty()) {
                    Text(text = chat.mensajes.last().contenido)
                }
            }
        }
    }
}