package com.example.unimarket.model

data class Mensaje(
    val id: String = "",
    val chatId: String = "",
    val contenido: String = "",
    val remitente: String = "",
    val timestamp: Long = 0
)
