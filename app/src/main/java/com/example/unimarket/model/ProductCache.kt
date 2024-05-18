package com.example.unimarket.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductCache
@Inject constructor() {
    private val cache: MutableMap<String, Product> = mutableMapOf()

    fun putProduct(key: String, product: Product) {
        cache[key] = product
    }

    fun getProduct(key: String): Product? {
        return cache[key]
    }

    fun getProducts(): List<Product> {
        return cache.values.toList()
    }

    fun clearCache() {
        cache.clear()
    }
}