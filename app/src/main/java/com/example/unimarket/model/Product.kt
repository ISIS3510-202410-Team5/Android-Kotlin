package com.example.unimarket.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.unimarket.entities.ProductoEntity

data class Product(
    val id: String,
    val coverUrl: String,
    val title: String,
    val precio: String,
    val latitud: String,
    val longitud: String,
    val categories: String,
    val related: String
)
{
    constructor(): this("","","","","",""
        , "", "")


    fun convertToEntity(): ProductoEntity {
        return ProductoEntity(id, title, precio, coverUrl, latitud, longitud, related, categories)
    }
}
