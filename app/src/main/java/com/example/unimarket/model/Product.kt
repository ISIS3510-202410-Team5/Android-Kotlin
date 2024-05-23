package com.example.unimarket.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.unimarket.entities.ProductoEntity
import java.text.SimpleDateFormat
import java.util.Date

data class Product(
    val id: String,
    val coverUrl: String,
    val title: String,
    val precio: String,
    val latitud: String,
    val longitud: String,
    val categories: String,
    val related: String,
    val fecha_publicacion: Date,
    val proveedor: String
)
{
    constructor(): this("","","","","",""
        , "", "",Date(),"")


    fun convertToEntity(): ProductoEntity {

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")

        val dateString: String = sdf.format(fecha_publicacion)

        return ProductoEntity(id, title, precio, coverUrl, latitud, longitud, related, categories,dateString,proveedor)
    }
}