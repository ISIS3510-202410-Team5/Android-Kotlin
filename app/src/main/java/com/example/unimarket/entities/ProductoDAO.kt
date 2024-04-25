package com.example.unimarket.entities

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Delete
import com.google.firebase.firestore.auth.User
import kotlinx.coroutines.flow.Flow


@Dao
interface ProductoDAO {
    @Query("SELECT * FROM shopping_cart")
    fun getAllProducts(): List<ProductoEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingCache(product: ProductoEntity)

    @Query("DELETE FROM shopping_cart")
    fun deleteAll()

    @Delete
    fun deleteProducto(vararg product: ProductoEntity)



}