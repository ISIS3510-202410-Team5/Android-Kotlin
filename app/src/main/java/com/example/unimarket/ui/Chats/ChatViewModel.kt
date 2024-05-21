package com.example.unimarket.ui.Chats

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unimarket.model.Chat
import com.example.unimarket.model.Mensaje
import com.example.unimarket.model.Product
import com.example.unimarket.repositories.ChatRepository
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(private val chatRepository: ChatRepository) : ViewModel() {

    val _chats = MutableStateFlow<List<Chat>>(emptyList())
    val chats: StateFlow<List<Chat>> = _chats

    private val _mensajes = MutableStateFlow<List<Mensaje>>(emptyList())
    val mensajes: StateFlow<List<Mensaje>> = _mensajes

    private val db = FirebaseFirestore.getInstance()

    private val _chatDetails = MutableStateFlow<Chat?>(null)
    val chatDetails: StateFlow<Chat?> = _chatDetails

    fun iniciarChat(product: Product, userCorreo: String, onChatCreated: (String) -> Unit) {
        viewModelScope.launch {
            try {
                val chatId = createChat(product, userCorreo)
                onChatCreated(chatId)
            } catch (e: Exception) {
                // Maneja el error si es necesario
            }
        }
    }

    private suspend fun createChat(product: Product, userCorreo: String): String {
        val chatRef = db.collection("chats").document()
        val chat = Chat(
            id = chatRef.id,
            emailProveedor = product.proveedor,
            emailCliente = userCorreo,
            productoId = product.id,
            mensajes = emptyList()
        )
        chatRef.set(chat).await()
        return chat.id
    }

    fun enviarMensaje(chatId: String, contenido: String, remitente: String) {
        viewModelScope.launch {
            chatRepository.enviarMensaje(chatId, contenido, remitente)
            obtenerMensajes(chatId)
        }
    }

    fun obtenerChats(email: String) {
        viewModelScope.launch(Dispatchers.Main) {
            val chats = chatRepository.obtenerChats(email)
            _chats.value = chats
        }
    }

    fun obtenerMensajes(chatId: String) {
        viewModelScope.launch {
            val mensajes = chatRepository.obtenerMensajes(chatId)
            _mensajes.value = mensajes
        }
    }

    fun obtenerChatDetails(chatId: String) {
        viewModelScope.launch {
            val chatDoc = db.collection("chats").document(chatId).get().await()
            val chat = chatDoc.toObject(Chat::class.java)?.copy(id = chatDoc.id)
            _chatDetails.value = chat
        }
    }

}