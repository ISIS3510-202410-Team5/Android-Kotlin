package com.example.unimarket.model

import android.util.ArrayMap
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class CategoryCache
@Inject constructor(){

    private val cache: ArrayMap<String, List<Product>> = ArrayMap()

    fun putProducts(key: String, products: List<Product>) {
        cache[key] = products
    }

    fun getProducts(key: String) : List<Product>? {
        return cache[key]
    }
    fun clearCache() {
        cache.clear()
    }
}