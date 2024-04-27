package com.example.unimarket.ui.publishitem

import android.app.Application
import androidx.lifecycle.ViewModel
import com.example.unimarket.connection.NetworkConnectivityObserver
import com.example.unimarket.model.Product
import com.example.unimarket.repositories.ProductoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.lang.Appendable
import javax.inject.Inject


@HiltViewModel
class PublishItemViewModel
@Inject
constructor(
    private val productoRepository: ProductoRepository,
    private val application: Application
)
    : ViewModel()
{
    private val _productTitle = MutableStateFlow<String>("")
    val productTitle: StateFlow<String> = _productTitle

    private val _productPrice = MutableStateFlow<String>("0")
    val productPrice: StateFlow<String> = _productPrice

    val connectivityObserver = NetworkConnectivityObserver(application.applicationContext)


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