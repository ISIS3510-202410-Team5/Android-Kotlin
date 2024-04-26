package com.example.unimarket.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.unimarket.model.Product

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
    val categories: String
) {

    fun entityToDTO(): Product {
        return Product(id, coverUrl, title, precio, latitud, longitud, categories, related)
    }
}