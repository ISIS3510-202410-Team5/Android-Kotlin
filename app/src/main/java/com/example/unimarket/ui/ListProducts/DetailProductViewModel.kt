package com.example.unimarket.ui.ListProducts

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unimarket.model.Product
import com.example.unimarket.model.ProductCache
import com.example.unimarket.model.ShoppingCart
import com.example.unimarket.repositories.ConnectivityRepository
import com.example.unimarket.repositories.ProductoRepository
import com.example.unimarket.repositories.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class DetailProductViewModel
@Inject
constructor(
    private val productCache: ProductCache,
    private val shoppingCart: ShoppingCart
)
    : ViewModel() {

    private val _productListState: MutableState<List<Product>> = mutableStateOf(emptyList())
    val productListState: State<List<Product>> = _productListState
    var producto: MutableState<Product> = mutableStateOf(Product())

    init {
        cargarProductos()
    }

    private fun cargarProductos() {
        viewModelScope.launch(context = Dispatchers.Main) {
            val productList = withContext(Dispatchers.Main){productCache.getProducts("products") ?: emptyList()}

            _productListState.value = productList
            }
    }

    suspend fun buscarProductoEnLista(searchTerm: String): Product? {
        val productList = productListState.value ?: emptyList()
        val searchTermInt = searchTerm.toIntOrNull()

        for (product in productList) {
            if (searchTermInt != null && product.id.toIntOrNull() == searchTermInt) { // Comparar con el ID convertido a Int
                return product
            }
        }
        return null
    }

    fun addToShoppingCart(product: Product){
        shoppingCart.addProduct(product)
    }
}