package com.example.unimarket.repositories

import android.util.SparseArray
import com.example.unimarket.model.Product
import com.google.firebase.firestore.CollectionReference
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
        //primero debe cargar desde sharedPreferences al cache
    }


    fun addNewProduct(title: String, price: String, categories: String)
    {
        try
        {
            val product = createProduct(title = title, precio = price, categories = categories)
            productoDB.document(product.id).set(product)
        }
        catch (e: Exception)
        {
            //Hacer algo para agarrar el error?
            e.printStackTrace()
        }
    }



    fun savePostFieldDataCache(fieldId: Int, value: String) {
        postCache.put(fieldId, value)

        //guardar tambien en memoria - considerar hacerlo en otro momento
    }

    fun getPostFieldDataCache(fieldId: Int): String? {
        return postCache.get(fieldId)
    }

    fun clearCache() {
        postCache.clear()
    }
    


    private fun createProduct(coverURL: String = "", title: String, precio: String, categories: String): Product {
        /*TODO cambiar la logica de como obtener el cover URL
        *  TODO: Incluir como sacar latitud y longitud de cuando se llama esta funcion */
        val id = UUID.randomUUID().toString()
        val latitud = ""
        val longitud = ""
        return Product(id, coverURL, title, precio, latitud, longitud, categories, "")
    }
}