package com.example.unimarket.ui.categoryList

import android.app.Application
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unimarket.connection.ConnectivityObserver
import com.example.unimarket.connection.NetworkConnectivityObserver
import com.example.unimarket.model.Product
import com.example.unimarket.repositories.Result
import com.example.unimarket.repositories.CategoryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class CategoryViewModel
@Inject
constructor(
    private val categoryRepository: CategoryRepository,
    private val application: Application
) : ViewModel(){

    private val _category = MutableStateFlow<String>(value="")

    private val _productList = MutableStateFlow<List<Product>>(value = listOf())
    val productList : StateFlow<List<Product>> = _productList

    val connectivityObserver = NetworkConnectivityObserver(application.applicationContext)



    fun assignCategory(category: String){
        _category.value = category
        obtenerProductosFirebase()
        obtenerProductosCache()



    }

    private fun obtenerProductosCache(){
        _productList.value = categoryRepository.getProductsCategoryCache(_category.value)
        Log.d("CategoryViewModel", categoryRepository.getProductsCategoryCache(_category.value).toString())
    }


    private fun obtenerProductosFirebase(){
        Log.d("CategoryViewModel", "ObtenerProductosFirebase() invoqued")

        val res = categoryRepository.getProductsCategory(_category.value).onEach {
            result ->

            when(result)
            {
                is Result.Error -> {
                    Log.d("CategoryViewModel", "Failed in retrieving categories")
                }
                is Result.Loading -> {
                    Log.d("CategoryViewModel", "Loading...")
                }
                is Result.Success -> {
                    Log.d("CategoryViewModel", "Worked Correctly")
                    if (result.data!!.isNotEmpty())
                    {
                        _productList.value = result.data
                    }

                }
                else -> {}
            }
        }.launchIn(viewModelScope)
    }

}