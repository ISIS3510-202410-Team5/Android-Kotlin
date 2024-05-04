package com.example.unimarket.ui.ListProducts


import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.unimarket.model.ProductCache
import com.example.unimarket.model.ShoppingCart
import com.example.unimarket.repositories.ConnectivityRepository
import com.example.unimarket.repositories.ProductoRepository
import com.example.unimarket.repositories.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class ProductListViewModel
@Inject
constructor
    (

            private val productoRepository: ProductoRepository,
            private val shoppingCart: ShoppingCart,
            private val connectivityRepository: ConnectivityRepository,
            private val productCache: ProductCache

): ViewModel()
{

    private val _state: MutableState<ProductListState> = mutableStateOf(ProductListState())
    val state: State<ProductListState> = _state

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    var isOnline: Flow<Boolean> = connectivityRepository.isConnected

    init{

        viewModelScope.launch {
            connectivityRepository.isConnected.collect() {
                Log.d("List", "$it")

            }
        }

        Log.d("", "inicia viewmodel")

        getProductList()
    }

    fun getProductList()
    {
        if (productCache.getProducts().isEmpty()) {

            productoRepository.getProductList().onEach { result ->

                when (result) {
                    is Result.Error -> {

                        _state.value =
                            ProductListState(error = result.message ?: "Error inesperado")
                    }

                    is Result.Loading -> {

                        _state.value = ProductListState(isLoading = true)
                    }

                    is Result.Success -> {

                        Log.d("", "esto es una prueba")

                        _state.value =
                            ProductListState(productos = result.data ?: emptyList())

                        viewModelScope.launch(Dispatchers.IO) {
                            _state.value.productos.forEach { product ->
                                productCache.putProduct(product.id, product)
                            }
                        }
                    }

                    else -> {}
                }
            }.launchIn(viewModelScope)

        }else {

            viewModelScope.launch(Dispatchers.IO) {
                val productListState =
                    ProductListState(productos = productCache.getProducts() ?: emptyList())
                withContext(Dispatchers.Main) {
                    _state.value = productListState
                }
            }

        }

    }
    fun onClear() {
        viewModelScope.cancel()
    }
}
