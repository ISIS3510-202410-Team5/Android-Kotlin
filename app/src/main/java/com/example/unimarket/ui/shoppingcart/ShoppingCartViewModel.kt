package com.example.unimarket.ui.shoppingcart

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.unimarket.model.Product
import com.example.unimarket.model.ShoppingCart
import com.example.unimarket.repositories.CarritoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class ShoppingCartViewModel
    @Inject constructor(
        private val shoppingCart: ShoppingCart,
        private val shoppingCartRepository: CarritoRepository): ViewModel() {



    private val _cartContent = MutableStateFlow<List<Product>>(value = listOf())
    val cartContent: StateFlow<List<Product>> = _cartContent

    private val _cartPrice = MutableStateFlow(0)
    val cartPrice: StateFlow<Int> = _cartPrice

    init {
        getCartContent()
        getCartPrice()
        shoppingCartRepository.test()
    }

    private fun getCartContent() {
        _cartContent.value = shoppingCart.getCart()
    }


    private fun getCartPrice() {
        _cartPrice.value = shoppingCart.getCartPrice()
    }


    fun removeProduct(index: Int){
        shoppingCart.removeProduct(index)
        getCartContent()
        getCartPrice()

    }



}