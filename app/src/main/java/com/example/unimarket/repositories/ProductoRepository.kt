package com.example.unimarket.repositories

import android.util.Log
import com.example.unimarket.model.Product
import com.example.unimarket.model.ProductCache
import com.example.unimarket.repositories.Result
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class ProductoRepository
@Inject
constructor(
    private val productCache: ProductCache,
    @Named("productos")
    private val productList: CollectionReference)
{

    // Esta función creo que deberia quitarse y pasar esa logica a postRepository
    fun addNewProduct(product: Product)
    {

        try
        {

            productList.document(product.id).set(product)


        }
        catch (e: Exception)

        {
            e.printStackTrace()
        }

    }

    fun getRelatedProducts(product: Product): Result<List<Product>> {
        return try {
            val relatedIds = product.related.split(",") ?: return Result.Success(emptyList()) // Si no hay productos relacionados, devolvemos una lista vacía
            val relatedProducts = mutableListOf<Product>()
            for (relatedId in relatedIds) {
                val relatedProduct = productCache.getProduct(relatedId)
                if (relatedProduct != null) {
                    relatedProducts.add(relatedProduct)
                }
            }
            Result.Success(data = relatedProducts)
        } catch (e: Exception) {
            Log.e("ProductoRepository", "Error al obtener los productos relacionados", e)
            Result.Error(message = e.localizedMessage ?: "Error desconocido")
        }
    }

    fun getProductList(): Flow<Result<List<Product>>> = flow{

        try {

            emit(Result.Loading<List<Product>>())

            val productosList = productList.get().await().map{document -> document.toObject(Product::class.java)}

            emit(Result.Success<List<Product>>(data = productosList))

        }

        catch (e: Exception)
        {
            emit(Result.Error<List<Product>>(message = e.localizedMessage ?: "Error desco"))
        }
    }




}