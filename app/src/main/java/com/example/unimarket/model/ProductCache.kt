package com.example.unimarket.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductCache
@Inject constructor() {
    private val cache: MutableMap<String, List<Product>> = mutableMapOf()

    fun putProducts(key: String, products: List<Product>) {
        cache[key] = products
    }

    fun getProducts(key: String): List<Product>? {
        return cache[key]
    }

    fun clearCache() {
        cache.clear()
    }
}