package com.example.unimarket.ui.ListProducts

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unimarket.model.Product
import com.example.unimarket.model.ShoppingCart
import com.example.unimarket.repositories.ProductoRepository
import com.example.unimarket.repositories.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.math.log

@HiltViewModel
class ProductListViewModel
@Inject
constructor
    (

            private val productoRepository: ProductoRepository,
            private val shoppingCart: ShoppingCart

): ViewModel()
{

    private val _state: MutableState<ProductListState> = mutableStateOf(ProductListState())
    val state: State<ProductListState> = _state

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    init{
        getProductList()
    }

    fun getProductList()
    {
        productoRepository.getProductList().onEach { result ->

            when(result){
                is Result.Error -> {

                    _state.value = ProductListState(error = result.message?: "Error inesperado")
                }
                is Result.Loading -> {

                    _state.value = ProductListState(isLoading = true)
                }
                is Result.Success ->  {

                    _state.value = ProductListState(productos = result.data ?: emptyList())
                }

                else -> {}
            }
        }.launchIn(viewModelScope)
    }

    fun addToShoppingCart(product: Product){
        shoppingCart.addProduct(product)
    }
    suspend fun fetchRelatedProduct(productId: String): Result<List<String>> {
        return try {
            val result = productoRepository.getRelatedById(productId)
            if (result is Result.Success) {

                val data = result.data?.split(",") ?: emptyList()
                Log.d("Split realizado, creacion lista","$data")
                Result.Success(data = data)
            } else {
                Result.Error(message = "Error desconocido")
            }
        } catch (e: Exception) {
            Log.d("errorVM", "ErrorVM")
            Result.Error(message = e.localizedMessage ?: "Error desconocido")
        }
    }





}