package com.example.unimarket.entities

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow


@Dao
interface ProductoDAO {
    @Query("SELECT * FROM shopping_cart")
    fun getAllProducts(): Flow<List<ProductoEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingCache(products: List<ProductoEntity>)

    @Query("DELETE FROM shopping_cart")
    fun deleteAll()



}