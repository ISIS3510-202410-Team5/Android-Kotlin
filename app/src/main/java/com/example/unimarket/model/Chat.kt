package com.example.unimarket.model

data class Chat(
    val id: String = "",
    val productoId: String = "",
    val emailCliente: String = "",
    val emailProveedor: String = "",
    val mensajes: List<Mensaje> = listOf()
)