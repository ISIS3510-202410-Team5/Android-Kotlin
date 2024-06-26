package com.example.unimarket.ui.Chats

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unimarket.model.Chat
import com.example.unimarket.model.Mensaje
import com.example.unimarket.model.Product
import com.example.unimarket.model.ProductCache
import com.example.unimarket.model.ShoppingCart
import com.example.unimarket.repositories.ChatRepository
import com.example.unimarket.repositories.ConnectivityRepository
import com.example.unimarket.repositories.ProductoRepository
import com.example.unimarket.repositories.Result
import com.example.unimarket.ui.ListProducts.ProductListState
import com.google.firebase.firestore.FirebaseFirestore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
@HiltViewModel
class ChatProductViewModel @Inject constructor(
    private val productoRepository: ProductoRepository,
    private val connectivityRepository: ConnectivityRepository,
    private val productCache: ProductCache
) : ViewModel() {

    private val _state: MutableState<ProductListState> = mutableStateOf(ProductListState())
    val state: State<ProductListState> = _state

    var isOnline: Flow<Boolean> = connectivityRepository.isConnected

    var selectedProduct = MutableLiveData<Product?>()

    private val _providerProductsState: MutableState<ProductListState> = mutableStateOf(ProductListState())
    val providerProductsState: State<ProductListState> = _providerProductsState

    fun getProductById(productId: String) {

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

                            _state.value = ProductListState(productos = result.data ?: emptyList())

                            viewModelScope.launch(Dispatchers.IO) {
                                _state.value.productos.forEach { product ->
                                    productCache.putProduct(product.id, product)
                                }
                            }

                            viewModelScope.launch(Dispatchers.Main) {

                                val product = _state.value.productos.find { it.id == productId }

                                if (product != null) {
                                    selectedProduct.postValue(product)
                                } else {
                                    Log.e("getProductById", "Producto con ID $productId no encontrado")
                                    selectedProduct.postValue(null)
                                }

                            }
                        }
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

            viewModelScope.launch(Dispatchers.Main) {

                val product = productCache.getProduct(productId)

                if (product != null) {
                    selectedProduct.postValue(product)
                } else {
                    Log.e("getProductById", "Producto con ID $productId no encontrado")
                    selectedProduct.postValue(null)
                }

            }

        }
    }

    fun getProductsByProvider(providerId: String) {

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
                        viewModelScope.launch(Dispatchers.Main) {

                            _providerProductsState.value = ProductListState(
                                productos = result.data?.filter { it.proveedor == providerId } ?: emptyList()
                            )

                        }

                    }
                }
            }.launchIn(viewModelScope)
        }else {

            viewModelScope.launch(Dispatchers.Main) {

                _providerProductsState.value = ProductListState(
                    productos = productCache.getProducts().filter { it.proveedor == providerId } ?: emptyList()
                )

            }
        }
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


                        viewModelScope.launch(Dispatchers.IO) {
                            _state.value = ProductListState(productos = result.data ?: emptyList())
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
}
