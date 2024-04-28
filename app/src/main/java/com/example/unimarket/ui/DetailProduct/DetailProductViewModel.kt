package com.example.unimarket.ui.DetailProduct

import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.unimarket.model.Product
import com.example.unimarket.model.ProductCache
import com.example.unimarket.model.RelatedCache
import com.example.unimarket.model.ShoppingCart
import com.example.unimarket.repositories.ConnectivityRepository
import com.example.unimarket.repositories.ProductoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.example.unimarket.repositories.Result
import com.example.unimarket.ui.ListProducts.ProductListState
import com.example.unimarket.ui.ListProducts.SelectedProductViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class DetailProductViewModel
@Inject
constructor(
    private val productCache: ProductCache,
    private val productoRepository: ProductoRepository,
    private val shoppingCart: ShoppingCart,
    private val relatedCache: RelatedCache,
    private val connectivityRepository: ConnectivityRepository
)
    : ViewModel() {

    private val _productListState: MutableState<List<Product>> = mutableStateOf(emptyList())

    val _selectedProduct = MutableLiveData<Product>()

    private val _productosRelacionados: MutableState<ProductListState> = mutableStateOf(ProductListState())
    val productosRelacionados: State<ProductListState> = _productosRelacionados

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    var isConnected: Flow<Boolean> = connectivityRepository.isConnected

    val context = LocalContext

    init {
        cargarProductos()
    }

    fun setSelectedProduct(product: Product) {
        _selectedProduct.value = product

        val cacheRelatedProducts = relatedCache.getProductsRelated(_selectedProduct.value!!.id)

        viewModelScope.launch(Dispatchers.Main) {
            isConnected.collect { isConnected ->

                if (isConnected) {

                    if(cacheRelatedProducts != null)
                    {

                        _productosRelacionados.value = ProductListState(productos = cacheRelatedProducts)

                    }else{

                        RelatedProducts()

                    }

                } else {

                    if(cacheRelatedProducts != null)
                    {

                        _productosRelacionados.value = ProductListState(productos = cacheRelatedProducts)

                    }else{


                    }

                }
            }
        }
    }

    private fun cargarProductos() {
        viewModelScope.launch {
            val productList = productCache.getProducts("products") ?: emptyList()
            _productListState.value = productList
        }
    }

    private suspend fun fetchRelatedProduct(): Result<List<Product>> {
        Log.d("DetailProductViewModel", "Productos relacionados encontrados:" + _selectedProduct.value?.id)
        val selectedProductId = _selectedProduct.value?.id ?: return Result.Error("No se ha seleccionado ningún producto")
        return withContext(Dispatchers.Default) {



            val result = productoRepository.getRelatedById(selectedProductId)
            if (result is Result.Success) {
                val products = mutableListOf<Product>()
                val data = result.data?.split(",") ?: emptyList()
                for (id in data) {
                    val product = _productListState.value.find { it.id == id }
                    product?.let { products.add(it) }
                }
                relatedCache.putProductsRelated(selectedProductId,products)
                Result.Success(data = products)
            } else {
                Result.Error(message = "Error desconocido")
            }




        }
    }

    fun RelatedProducts() {
        viewModelScope.launch {
            Log.d("DetailProductViewModel", "Buscando productos relacionados...")
            val result = fetchRelatedProduct()
            _productosRelacionados.value = when (result) {
                is Result.Success -> {
                    Log.d("DetailProductViewModel", "Productos relacionados encontrados: ${result.data}")
                    ProductListState(productos = result.data ?: emptyList())
                }
                is Result.Error -> {
                    Log.e("DetailProductViewModel", "Error al buscar productos relacionados: ${result.message}")
                    ProductListState(error = result.message ?: "Error desconocido")
                }

                is Result.Loading -> TODO()
            }
        }
    }

    fun addToShoppingCart(product: Product){
        shoppingCart.addProduct(product)
    }
}