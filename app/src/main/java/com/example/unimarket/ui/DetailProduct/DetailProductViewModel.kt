package com.example.unimarket.ui.DetailProduct

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unimarket.model.Product
import com.example.unimarket.model.ProductCache
import com.example.unimarket.model.ShoppingCart
import com.example.unimarket.repositories.ProductoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import com.example.unimarket.repositories.Result

@HiltViewModel
class DetailProductViewModel
@Inject
constructor(
    private val productCache: ProductCache,
    private val productoRepository: ProductoRepository,
    private val shoppingCart: ShoppingCart
)
    : ViewModel() {

    private val _productListState: MutableState<List<Product>> = mutableStateOf(emptyList())
    val productListState: State<List<Product>> = _productListState

    init {
        cargarProductos()
    }

    private fun cargarProductos() {
        viewModelScope.launch(context = Dispatchers.Main) {
            val productList = withContext(Dispatchers.IO){productCache.getProducts("products") ?: emptyList()}

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