package com.example.unimarket.ui.publishitem

import androidx.lifecycle.ViewModel
import com.example.unimarket.model.Product
import com.example.unimarket.repositories.ProductoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject


@HiltViewModel
class PublishItemViewModel
@Inject
constructor(
    private val productoRepository: ProductoRepository
)
    : ViewModel()
{
    private val _productTitle = MutableStateFlow<String>("")
    val productTitle: StateFlow<String> = _productTitle

    private val _productPrice = MutableStateFlow<String>("0")
    val productPrice: StateFlow<String> = _productPrice


    /*fun addProduct(){
        val newProduct = Product()
        newProduct.id =
    }*/

    fun onTitleChange(text:String){
        _productTitle.value = text
    }


    fun onPriceChange(text: String){
        _productPrice.value = text
    }



}