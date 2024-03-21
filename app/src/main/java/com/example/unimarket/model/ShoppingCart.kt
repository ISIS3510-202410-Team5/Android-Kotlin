package com.example.unimarket.model

import android.util.Log
import javax.inject.Singleton
import javax.inject.Inject

@Singleton
class ShoppingCart @Inject constructor() {

    private val productList: MutableList<Product> = mutableListOf()


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


    fun removeProduct(index: Int) {
        try {
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
            productList.clear()
        } catch (e:Exception){
            Log.d(null, "Buy operation failed")
        }
    }

    fun addProduct(product: Product){
        Log.d(null, "addProductCalled :D")
        try{
            if (!productList.contains(product)){
                productList.add(product)
            }
        } catch (e: Exception){
            Log.d(null, "Failed to add product to shopping cart")
        }
    }

}