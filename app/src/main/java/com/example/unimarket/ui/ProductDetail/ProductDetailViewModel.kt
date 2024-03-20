package com.example.unimarket.ui.ProductDetail

import android.icu.text.CaseMap.Title
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.Recomposer
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.unimarket.model.Product
import com.example.unimarket.repositories.ProductoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.UUID
import javax.inject.Inject
@HiltViewModel
class ProductDetailViewModel

@Inject
constructor(private val productoRepository: ProductoRepository): ViewModel()

{

    private val _state: MutableState<ProductDetailState> = mutableStateOf(ProductDetailState())
    val state: State<ProductDetailState>
        get()= _state

    fun addNewProduct(title: String, precio: String)
    {

        val product = Product(

            id = UUID.randomUUID().toString(),
            coverUrl = "",
            title = title,
            precio = precio

        )

        productoRepository.addNewProduct(product)


    }

}