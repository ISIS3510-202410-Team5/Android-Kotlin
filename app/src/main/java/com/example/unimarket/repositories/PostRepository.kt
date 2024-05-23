package com.example.unimarket.repositories

import android.util.SparseArray
import com.example.unimarket.di.SharedPreferenceService
import com.example.unimarket.model.Product
import com.google.firebase.firestore.CollectionReference
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton


@Singleton
class PostRepository
@Inject
constructor(
    @Named("productos")
    private val productoDB: CollectionReference)
{
    private val postCache: SparseArray<String> = SparseArray()



    init {
        postCache.put(0, SharedPreferenceService.getProductCover())
        postCache.put(1, SharedPreferenceService.getProductTitle())
        postCache.put(2, SharedPreferenceService.getProductPrice())
        postCache.put(3, SharedPreferenceService.getProductCategories())
    }


    fun addNewProduct(coverURL: String, title: String, price: String, categories: String)
    {
        try
        {
            val product = createProduct(coverURL = coverURL,title = title, precio = price, categories = categories)
            productoDB.document(product.id).set(product)
        }
        catch (e: Exception)
        {
            //Hacer algo para agarrar el error?
            e.printStackTrace()
        }
    }



    suspend fun savePostFieldDataCache(fieldId: Int, value: String) {
        postCache.put(fieldId, value)

        when(fieldId) {
            0 -> SharedPreferenceService.putProductURL(value)
            1 -> SharedPreferenceService.putProductTitle(value)
            2 -> SharedPreferenceService.putProductPrice(value)
            3 -> SharedPreferenceService.putProductCategories(value)
        }
    }

    fun getPostFieldDataCache(fieldId: Int): String? {
        return postCache.get(fieldId)
    }

    fun clearCache() {
        postCache.clear()
    }

    suspend fun clearSharedPreferences() {
        SharedPreferenceService.clearProductPreferences()
    }
    


    private fun createProduct(coverURL: String = "", title: String, precio: String, categories: String): Product {
        /*
        *  TODO: Incluir como sacar latitud y longitud de cuando se llama esta funcion */
        val id = UUID.randomUUID().toString()
        val latitud = ""
        val longitud = ""
        return Product(id, coverURL, title, precio, latitud, longitud, categories, "", Date(), SharedPreferenceService.getCurrentUser() ?: "")
    }
}