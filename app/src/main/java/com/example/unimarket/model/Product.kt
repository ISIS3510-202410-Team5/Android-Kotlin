package com.example.unimarket.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Product(
    val stringResourceId: String,
    @DrawableRes val imageResourceId: Int
)
