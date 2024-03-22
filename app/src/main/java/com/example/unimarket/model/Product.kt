package com.example.unimarket.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Product(
    val id: String,
    val coverUrl: String,
    val title: String,
    val precio: String,
    val latitud: String,
    val longitud: String
)
{
    constructor(): this("","","","","","")
}
