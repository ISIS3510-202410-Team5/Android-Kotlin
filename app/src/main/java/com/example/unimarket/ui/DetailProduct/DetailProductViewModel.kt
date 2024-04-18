package com.example.unimarket.ui.DetailProduct

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unimarket.model.Product
import com.example.unimarket.model.ProductCache
import com.example.unimarket.model.ShoppingCart
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
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