package com.example.unimarket.ui.DetailProduct

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unimarket.model.Product
import com.example.unimarket.model.ProductCache
import com.example.unimarket.model.ShoppingCart
import com.example.unimarket.repositories.ConnectivityRepository
import com.example.unimarket.repositories.ProductoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import com.example.unimarket.ui.ListProducts.ProductListState
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@HiltViewModel
class DetailProductViewModel
@Inject
constructor(
    private val productCache: ProductCache,
    private val productoRepository: ProductoRepository,
    private val shoppingCart: ShoppingCart
)
    : ViewModel() {


        val _selectedProduct = MutableLiveData<Product>()


    private val _productosRelacionados: MutableState<ProductListState> = mutableStateOf(ProductListState())
    val productosRelacionados: State<ProductListState> = _productosRelacionados

    private val _isRefreshing = MutableStateFlow(false)
    
    val context = LocalContext

    fun getRelatedProductsView(productid: String) {

        _selectedProduct.value = productCache.getProduct(productid)

        val selectedProduct = _selectedProduct.value ?: return

        val relateProducts = productoRepository.getRelatedProducts(selectedProduct)

        _productosRelacionados.value = ProductListState(productos = relateProducts.data ?: emptyList())

    }

    fun addToShoppingCart(product: Product){
        shoppingCart.addProduct(product)
    }
}