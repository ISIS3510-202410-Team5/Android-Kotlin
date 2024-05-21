package com.example.unimarket.entities

import android.annotation.SuppressLint
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.unimarket.model.Product
import java.text.SimpleDateFormat
import java.util.Date

@Entity(tableName="shopping_cart")
data class ProductoEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "precio")
    val precio: String,
    @ColumnInfo(name = "coverUrl")
    val coverUrl: String,
    @ColumnInfo(name = "latitud")
    val latitud: String,
    @ColumnInfo(name = "longitud")
    val longitud: String,
    @ColumnInfo(name = "related")
    val related: String,
    @ColumnInfo(name = "categories")
    val categories: String,
    @ColumnInfo(name = "fecha_publicacion")
    val fecha_publicacion: String,
    @ColumnInfo(name = "proveedor")
    val proveedor: String
) {

    fun entityToDTO(): Product {

        val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm:ss")

        val date: Date = sdf.parse(fecha_publicacion)

        return Product(id, coverUrl, title, precio, latitud, longitud, categories, related,date,proveedor)
    }
}