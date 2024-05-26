package com.example.unimarket.ui.Chats

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import coil.request.CachePolicy
import com.example.unimarket.R
import com.example.unimarket.di.SharedPreferenceService
import com.example.unimarket.model.Chat
import com.example.unimarket.ui.navigation.Screen
import com.example.unimarket.ui.usuario.GlideImage
import com.example.unimarket.ui.usuario.HeaderImage
import com.example.unimarket.ui.usuario.PerfilViewModel
import com.example.unimarket.ui.usuario.ProfileImage

@Composable
fun ListaDeChats(chatViewModel: ChatViewModel, navController: NavHostController, perfilViewModel: PerfilViewModel) {

    val chats by chatViewModel.chats.collectAsState(emptyList())

    val context = LocalContext.current

    var isEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(chatViewModel.isOnline) {
        chatViewModel.isOnline.collect { isOnline ->
            isEnabled = isOnline
            if (isOnline)
            {
                SharedPreferenceService.getCurrentUser()?.let { chatViewModel.obtenerChats(it) }
            }
            if (!isOnline) {
                Toast.makeText(context, "No hay conexión. El contenido puede que esté desactualizado.", Toast.LENGTH_LONG).show()
            }
        }

    }

        Column {

            Text(
                text = "Lista de Chats",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )

            Spacer(modifier = Modifier.size(14.dp))

            if (isEnabled) {

                LazyColumn(modifier = Modifier.fillMaxSize()) { // Use fillMaxSize to occupy the available space
                    items(chats) { chat ->
                        ChatItem( // Create a reusable ChatItem composable
                            chat = chat,
                            onClick = {
                                navController.navigate(Screen.ChatDetail.route + "/${chat.id}")
                            },
                            perfilViewModel = perfilViewModel
                        )
                    }
                }

            }

            else {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column {

                        Text(
                            text = "Oops...",
                            style = MaterialTheme.typography.headlineMedium,
                            color = Color.Red
                        )
                        Text(
                            text = "No hay conexión a Internet",
                            style = MaterialTheme.typography.titleLarge,
                            color = Color.Red
                        )

                    }
                }
            }

        }

}

@Composable
fun ChatItem(
    chat: Chat,
    onClick: () -> Unit
    ,perfilViewModel: PerfilViewModel
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

            if(SharedPreferenceService.getCurrentUser()!=chat.emailProveedor)
            {

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
            else
            {

                Column(modifier = Modifier.weight(1f)) { // Expand content within remaining space
                    Text(
                        text = "Chat con: ${chat.emailCliente}",
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
}
