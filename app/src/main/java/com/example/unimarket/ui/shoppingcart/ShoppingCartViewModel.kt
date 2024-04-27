package com.example.unimarket.ui.shoppingcart

import android.app.Application
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unimarket.connection.NetworkConnectivityObserver
import com.example.unimarket.repositories.Result
import com.example.unimarket.model.Product
import com.example.unimarket.model.ShoppingCart
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ShoppingCartViewModel
    @Inject constructor(
        private val shoppingCart: ShoppingCart,
        private val application: Application): ViewModel() {



    private val _cartContent = MutableStateFlow<List<Product>>(value = listOf())
    val cartContent: StateFlow<List<Product>> = _cartContent

    private val _cartPrice = MutableStateFlow(0)
    val cartPrice: StateFlow<Int> = _cartPrice

    val connectivityObserver = NetworkConnectivityObserver(application.applicationContext)

    init {
        Log.d("ShoppingCartViewModel", "invoque init function")
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                getCartContent()
                getCartPrice()
            }
        }

    }

    private fun getCartContent() {
        _cartContent.value = shoppingCart.getCart()
    }


    private fun getCartPrice() {
        _cartPrice.value = shoppingCart.getCartPrice()
    }



    fun removeProduct(index: Int){
        shoppingCart.removeProduct(index)
        viewModelScope.launch{
            withContext(Dispatchers.Main) {
                getCartContent()
                getCartPrice()
            }
        }
    }

    fun pressPayment() {
        shoppingCart.buyCart()
        viewModelScope.launch{
            withContext(Dispatchers.Main) {
                getCartContent()
                getCartPrice()
            }
        }
    }



}