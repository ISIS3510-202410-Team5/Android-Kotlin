package com.example.unimarket.ui.publishitem

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unimarket.connection.NetworkConnectivityObserver
import com.example.unimarket.model.Product
import com.example.unimarket.repositories.PostRepository
import com.example.unimarket.repositories.ProductoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.Appendable
import javax.inject.Inject


@HiltViewModel
class PublishItemViewModel
@Inject
constructor(
    private val postRepository: PostRepository,
    private val application: Application
)
    : ViewModel()
{

    private val _productTitle = MutableStateFlow<String>("")
    val productTitle: StateFlow<String> = _productTitle

    private val _productPrice = MutableStateFlow<String>("0")
    val productPrice: StateFlow<String> = _productPrice

    private val _productCategories = MutableStateFlow<String>("")
    val productCategories: StateFlow<String> = _productCategories

    val connectivityObserver = NetworkConnectivityObserver(application.applicationContext)

    init {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                //Cargar del cache a los estados actuales
                val nullableUri = postRepository.getPostFieldDataCache(0)
                val nullableTitle = postRepository.getPostFieldDataCache(1)
                val nullablePrice = postRepository.getPostFieldDataCache(2)
                val nullableCategories = postRepository.getPostFieldDataCache(3)

                withContext(Dispatchers.Default){
                    //if (nullableUri != null) _productUri.value = nullableTitle
                    if (nullableTitle != null) _productTitle.value = nullableTitle
                    if (nullablePrice != null) _productPrice.value = nullablePrice
                    if (nullableCategories != null) _productCategories.value = nullableCategories
                }
            }
        }
    }


    /*fun addProduct(){
        val newProduct = Product()
        newProduct.id =
    }*/

    fun onTitleChange(text:String){
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _productTitle.value = text
            }
            withContext(Dispatchers.IO){
                postRepository.savePostFieldDataCache(1, text)
            }
        }
    }


    fun onPriceChange(text: String){
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _productPrice.value = text
            }
            withContext(Dispatchers.IO) {
                postRepository.savePostFieldDataCache(2, text)
            }
        }
    }

    fun onCategoriesChange(text: String){
        viewModelScope.launch {
            withContext(Dispatchers.Main) {
                _productCategories.value = text

            }
            withContext(Dispatchers.IO) {
                postRepository.savePostFieldDataCache(3, text)
            }
        }
    }



    fun addProductDB() {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                postRepository.addNewProduct(_productTitle.value, _productPrice.value, _productCategories.value)
                postRepository.clearCache()
                //postRepository.clearSharedPreferences()

                withContext(Dispatchers.Main) {
                    _productTitle.value = ""
                    _productPrice.value = ""
                    _productCategories.value = ""
                }
            }
        }
    }



}