package com.example.unimarket.ui.ProductDetail

import com.example.unimarket.model.Product

data class ProductDetailState

    (

            val isLoading: Boolean = false,
            val producto: Product? = null,
            val error: String = ""
    )
