package com.example.unimarket.ui.ListProducts

import com.example.unimarket.model.Product

data class ProductListState(

    val isLoading: Boolean = false,
    val productos: List<Product> = emptyList(),
    val error: String = ""
)
