package com.example.unimarket.repositories

import android.util.Log
import com.example.unimarket.entities.CategoryDAO
import com.example.unimarket.entities.CategoryEntity
import com.example.unimarket.model.CategoryCache
import com.example.unimarket.model.Product
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton


@Singleton
class CategoryRepository
@Inject
constructor(
    @Named("productos")
    private val productCollection: CollectionReference,
    @Named("categories")
    private val categoryCollection: CollectionReference,
    private val categoryCache: CategoryCache,
    private val categoryDAO: CategoryDAO
){
    private val categoryList: MutableList<CategoryEntity> = mutableListOf()


    init{
        CoroutineScope(Dispatchers.Main).launch {
            categoryList.addAll(getCategoriesDB())
        }
    }


    fun getCategories(): List<CategoryEntity> {
        return categoryList
    }


    //Funciones para acceso al cache

    fun getProductsCategoryCache(category: String) : List<Product> {
        return categoryCache.getProducts(category) ?: listOf()
    }





    //Funciones para acceso a firebase firestore

    fun getProductsCategory(category: String): Flow<Result<List<Product>>> = flow{
        try {
            emit(Result.Loading<List<Product>>())

            var filteredList: List<Product>

            val unfilteredProductList = productCollection.get().await().map{ document -> document.toObject(Product::class.java)}

            CoroutineScope(Dispatchers.Default).launch {
                filteredList = unfilteredProductList.filter {it.categories.contains(category)}
                withContext(Dispatchers.IO) {
                    categoryCache.putProducts(category, filteredList)
                }

                withContext(Dispatchers.Main){
                    emit(Result.Success<List<Product>>(data = filteredList))
                }

            }



        }
        catch (e: Exception)
        {
            emit(Result.Error<List<Product>>(message = e.localizedMessage ?: "Unknown Error"))
        }
    }


    fun getCategoriesFirebase(): Flow<Result<List<String>>> = flow{
        Log.d("CategoryRepository", "Llamada a getCategoriesFirebase")
        try {
            emit(Result.Loading<List<String>>())

            val CategoryList = categoryCollection.document("recomend").get().await().get("recomend")!! as List<String>

            CoroutineScope(Dispatchers.IO).launch{
                for (index in CategoryList.indices) {
                    categoryDAO.insertCategories(category = CategoryEntity(id = index.toString(), catName = CategoryList[index]))
                }
            }

            emit(Result.Success<List<String>>(data = CategoryList))
        }
        catch (e: Exception)
        {
            emit(Result.Error<List<String>>(message = e.localizedMessage ?: "Unknown Error"))
        }
    }



    //Funciones para el acceso a la base de datos
    suspend fun getCategoriesDB(): List<CategoryEntity> {
        return withContext(Dispatchers.IO) {
            categoryDAO.getAllCategories()
        }
    }
}