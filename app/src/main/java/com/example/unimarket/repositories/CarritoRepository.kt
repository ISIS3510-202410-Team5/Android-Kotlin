package com.example.unimarket.repositories

import android.util.Log
import com.example.unimarket.entities.ProductoDAO
import com.example.unimarket.entities.ProductoEntity
import com.example.unimarket.model.Product
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class CarritoRepository
@Inject
constructor(
    @Named("productos")
    private val productoFirebaseDB : CollectionReference,

    private val productoDAO: ProductoDAO
){


    fun test() {
        Log.d("CarritoRepository", "Is working i guess")

        CoroutineScope(Dispatchers.IO).launch {
            productoDAO.deleteAll()
        }
    }

    //val productosRoomDB: Flow<List<ProductoEntity>> = productoDAO.getAllProducts()

    //val productosFireBase: Flow<List<Product>>




}