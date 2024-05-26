package com.example.unimarket.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ChatCache @Inject constructor() {
    private val chatsList: MutableList<Chat> = mutableListOf()

    fun saveChat(chat: Chat) {

        if (!chatsList.any { it.id == chat.id }) {
            chatsList.add(chat)
        }
    }

    fun getChats(): List<Chat> {
        return chatsList.toList()
    }

    fun clearCache() {
        chatsList.clear()
    }
}