package com.example.unimarket.ui.SearchProduct

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unimarket.di.SharedPreferenceService
import com.example.unimarket.model.Product
import com.example.unimarket.model.ProductCache
import com.example.unimarket.model.ShoppingCart
import com.example.unimarket.repositories.ConnectivityRepository
import com.example.unimarket.repositories.ProductoRepository
import com.example.unimarket.repositories.Result
import com.example.unimarket.ui.ListProducts.ProductListState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
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
class SearchVideModel @Inject
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


    fun filterUbication(context: Context, currentLocation: Location?, query: String, scope: CoroutineScope): LiveData<List<Product>> {
        val liveData = MutableLiveData<List<Product>>()

        val job = scope.launch(Dispatchers.Default) {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

            val result = if (isGpsEnabled) {
                _state.value.productos.filter { product ->
                    val distance = calculateDistance(
                        currentLocation?.latitude ?: 0.0,
                        currentLocation?.longitude ?: 0.0,
                        product.latitud.toDouble(),
                        product.longitud.toDouble()
                    )
                    product.title.contains(query, ignoreCase = true) && distance <= SharedPreferenceService.getLocationThreshold()
                }
            } else {
                _state.value.productos.filter { product ->
                    product.title.contains("dwadawdawdawdwadawdaw", ignoreCase = true)
                }
            }

            withContext(Dispatchers.Main) {
                liveData.value = result
            }
        }

        scope.launch {
            job.join()
        }

        return liveData
    }


    fun onClear() {
        viewModelScope.cancel()
    }
}