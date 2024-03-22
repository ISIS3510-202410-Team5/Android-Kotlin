package com.example.unimarket.repositories

import com.example.unimarket.model.Product
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
    @Named("productos")
    private val productList: CollectionReference)
{

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