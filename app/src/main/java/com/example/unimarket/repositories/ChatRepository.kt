package com.example.unimarket.repositories

import com.example.unimarket.model.Chat
import com.example.unimarket.model.Mensaje
import com.example.unimarket.model.Product
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.tasks.await

class ChatRepository {
    private val db = FirebaseFirestore.getInstance()

    suspend fun iniciarChat(producto: Product, emailCliente: String): String {
        val chat = hashMapOf(
            "productoId" to producto.id,
            "emailCliente" to emailCliente,
            "emailProveedor" to producto.proveedor
        )
        val result = db.collection("chats").add(chat).await()
        return result.id
    }

    suspend fun enviarMensaje(chatId: String, contenido: String, remitente: String) {
        val mensaje = hashMapOf(
            "chatId" to chatId,
            "contenido" to contenido,
            "remitente" to remitente,
            "timestamp" to System.currentTimeMillis()
        )
        db.collection("chats").document(chatId).collection("mensajes").add(mensaje).await()
    }

    suspend fun obtenerMensajes(chatId: String): List<Mensaje> {
        val snapshot = db.collection("chats").document(chatId).collection("mensajes")
            .orderBy("timestamp", Query.Direction.ASCENDING).get().await()
        return snapshot.documents.map { doc ->
            doc.toObject(Mensaje::class.java)!!.copy(id = doc.id)
        }
    }

    suspend fun obtenerChats(email: String): List<Chat> {
        val chatsCliente = db.collection("chats")
            .whereEqualTo("emailCliente", email)
            .get().await()
            .documents.map { doc ->
                val mensajes = obtenerMensajes(doc.id)
                doc.toObject(Chat::class.java)!!.copy(id = doc.id, mensajes = mensajes)
            }

        val chatsProveedor = db.collection("chats")
            .whereEqualTo("emailProveedor", email)
            .get().await()
            .documents.map { doc ->
                val mensajes = obtenerMensajes(doc.id)
                doc.toObject(Chat::class.java)!!.copy(id = doc.id, mensajes = mensajes)
            }

        return (chatsCliente + chatsProveedor).distinctBy { it.id }
    }

}