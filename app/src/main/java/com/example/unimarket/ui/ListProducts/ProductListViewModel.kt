package com.example.unimarket.ui.ListProducts

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.camera.core.impl.utils.ContextUtil.getApplicationContext
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.unimarket.model.Product
import com.example.unimarket.model.ProductCache
import com.example.unimarket.model.ShoppingCart
import com.example.unimarket.repositories.ConnectivityRepository
import com.example.unimarket.repositories.ProductoRepository
import com.example.unimarket.repositories.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.checkerframework.checker.units.qual.Current
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

    val isOnline = connectivityRepository.isConnected.asLiveData()

    var isConnected: Flow<Boolean> = connectivityRepository.isConnected

    init{
        getProductList()
    }

    fun getProductList()
    {
        isOnline.observeForever() { isOnline ->

            if (isOnline) {

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
                            productCache.putProducts("products", _state.value.productos)
                        }

                        else -> {}
                    }
                }.launchIn(viewModelScope)
            } else {

                _state.value = ProductListState(productos = productCache.getProducts("products")?: emptyList())

            }
        }

    }



}
