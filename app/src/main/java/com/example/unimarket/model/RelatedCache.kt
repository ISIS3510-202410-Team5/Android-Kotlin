package com.example.unimarket.model

import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class RelatedCache
@Inject constructor() {
    private var cache: Map<String, List<Product>> = emptyMap()

    fun putProductsRelated(key: String, products: List<Product>) {
        cache = cache + (key to products)
    }

    fun getProductsRelated(key: String): List<Product>? {
        return cache[key]
    }

    fun clearCache() {
        cache = emptyMap()
    }
}