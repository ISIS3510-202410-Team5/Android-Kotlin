package com.example.unimarket.ui.categoryList

import android.app.Application
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
        obtenerProductos()
    }

    private fun obtenerProductos(){

        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                connectivityObserver.observe().collect() {
                    if (it != ConnectivityObserver.Status.Available) {
                        _productList.value = categoryRepository.getProductsCategoryCache(_category.value)
                    } else {
                        categoryRepository.getProductsCategory(_category.value).onEach { result ->
                            when (result) {
                                is Result.Error -> _productList.value = listOf()
                                is Result.Loading -> _productList.value = listOf()
                                is Result.Success -> _productList.value = result.data ?: listOf()
                            }
                        }

                    }
                }
            }
        }


    }

}