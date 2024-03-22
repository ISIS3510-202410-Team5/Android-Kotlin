package com.example.unimarket.ui.home

import com.example.unimarket.model.User

data class UserListState(
    val isLoading: Boolean = false,
    val users: List<User> = emptyList(),
    val error: String = ""
)