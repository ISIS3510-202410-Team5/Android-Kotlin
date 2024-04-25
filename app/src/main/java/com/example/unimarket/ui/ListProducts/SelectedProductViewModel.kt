package com.example.unimarket.ui.ListProducts

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.unimarket.model.Product
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SelectedProductViewModel
@Inject
constructor() : ViewModel(){
    private val _selectedProduct = mutableStateOf<Product?>(null)

    fun setSelectedProduct(product: Product) {
        _selectedProduct.value = product
    }

    fun getSelectedProduct(): Product? {
        return _selectedProduct.value
    }
}