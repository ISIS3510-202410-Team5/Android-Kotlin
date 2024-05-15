package com.example.unimarket.repositories

import android.util.SparseArray
import com.example.unimarket.model.Product
import com.google.firebase.firestore.CollectionReference
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



    fun addNewProduct(product: Product)
    {
        try
        {
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
    


    fun createProduct(): Product {
        return Product()
    }
}