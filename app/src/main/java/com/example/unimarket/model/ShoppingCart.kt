package com.example.unimarket.model

import android.util.Log
import com.example.unimarket.entities.ProductoDAO
import com.example.unimarket.entities.ProductoEntity
import com.google.firebase.firestore.CollectionReference
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.util.Date
import javax.inject.Singleton
import javax.inject.Inject
import javax.inject.Named

@Singleton
class ShoppingCart
@Inject
constructor(
    @Named("productos")
    private val productoFirebaseDB: CollectionReference,
    private val productoDAO: ProductoDAO,
    @Named("shoppingCart")
    private val shoppingCartFirebaseDB: CollectionReference
) {

    private val productList: MutableList<Product> = mutableListOf()


    init {
        productList.addAll(getProductosRoomDB())
    }


    fun getCartPrice(): Int {
        var ret = 0
        productList.forEachIndexed{
            index, product ->
            try {
                ret += product.precio.toInt()
            } catch(e:Exception){
                Log.d("Cart Operation", "Product with id ${product.id} has invalid price value. Removing from Cart")
                productList.removeAt(index)
            }
        }
        return ret
    }

    fun getCart(): List<Product>{
        return productList
    }

    fun getProducto(index: Int): Product {
        Log.d("ShoppingCart", productList[index].toString())
        return productList.get(index)
    }


    fun removeProduct(index: Int) {
        try {
            removeFromDB(productList[index])
            productList.removeAt(index)
        } catch(e:Exception){
            Log.d(null, "Error, removeAt operation failed")
        }
    }


    fun buyCart() {
        try{
            productList.forEachIndexed { index, product ->
                Log.d(null, "Register buy of product $index, ${product.id}")
            }
            //should retrieve user's email here
            addPurchase(createPurchase(productList))
            removeAllFromDb()
            productList.clear()
        } catch (e:Exception){
            Log.d(null, "Buy operation failed")
        }
    }

    fun addProduct(product: Product) : Boolean{
        Log.d(null, "addProductCalled :D")
        return try{
            if (!productList.contains(product)){
                productList.add(product)
                insertDB(product)
                Log.d("ShoppingCart", "Is asynchronous")
                true
            } else {
                false
            }
        } catch (e: Exception){
            Log.d(null, "Failed to add product to shopping cart")
            false
        }
    }




    //Funciones privadas para el manejo de db

    private fun getProductosRoomDB(): List<Product>{

        var entityList : List<ProductoEntity>

        runBlocking {
            withContext(Dispatchers.IO) {
                entityList = productoDAO.getAllProducts()
            }
        }

        var ret = emptyList<Product>()
        runBlocking { withContext(Dispatchers.Default) {
            for (entity in entityList) {
                ret = ret + entity.entityToDTO()
            }
        }
        }

        return ret

    }

    private fun insertDB(productList: List<Product>) {
        for (product in productList){
            CoroutineScope(Dispatchers.IO).launch {
                Log.d("CarritoRepository", "Insert $product.id")
                productoDAO.insertShoppingCache(product.convertToEntity())
            }
        }
    }

    private fun insertDB(product: Product) {
        CoroutineScope(Dispatchers.IO).launch {
            delay(2000L)
            Log.d("ShoppingCart", "Insert $product.id")
            productoDAO.insertShoppingCache(product.convertToEntity())
        }
    }


    private fun removeFromDB(product: Product) {
        CoroutineScope(Dispatchers.IO).launch {
            productoDAO.deleteProducto(product.convertToEntity())
        }
    }

    private fun removeAllFromDb() {
        CoroutineScope(Dispatchers.IO).launch {
            productoDAO.deleteAll()
        }
    }


    private fun createPurchase(products: List<Product>, user: String = "") : Purchase {
        var str = ""
        for (producto in products){
            str += producto.id + ","
        }
        var newPurchase = Purchase(
            Date(),
            user,
            str)
        return newPurchase
    }

    private fun addPurchase(purchase: Purchase) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val purchaseCollection =
                    shoppingCartFirebaseDB.document("purchases").collection("date")
                purchaseCollection.add(purchase)
            } catch (e: Exception) {
                Log.d("ShoppingCart", "Failed to add purchase")
            }
        }
    }



    private fun updateCart(products: List<Product>) {
        Log.d("ShoppingCart", "Updates the Cart of the user")
    }

}