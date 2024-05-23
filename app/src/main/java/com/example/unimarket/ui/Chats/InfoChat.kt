package com.example.unimarket.ui.Chats

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.unimarket.di.SharedPreferenceService
import com.example.unimarket.ui.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoChat(navController: NavController, chatId: String, chatViewModel: ChatViewModel) {

    val chatDetails by chatViewModel.chatDetails.collectAsState()
    val usuarioActual: String? = SharedPreferenceService.getCurrentUser()

    LaunchedEffect(chatId) {
        chatViewModel.obtenerChatDetails(chatId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chat Information") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.ChatDetail.route + "/${chatId}") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp)
            ) {

                Text("Chat ID: $chatId", style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.height(8.dp))


                chatDetails?.let { Text(it.productoId, style = MaterialTheme.typography.bodySmall) }
                Spacer(modifier = Modifier.height(8.dp))

                if (chatDetails!!.emailProveedor != usuarioActual)
                {

                    Text("Chat con: " + chatDetails!!.emailProveedor, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(8.dp))

                }
            }
        }
    )
}