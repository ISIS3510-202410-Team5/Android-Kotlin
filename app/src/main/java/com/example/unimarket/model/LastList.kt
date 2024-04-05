package com.example.unimarket.model

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.example.unimarket.ui.ListProducts.ProductListState
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LastList @Inject constructor(

) {

    var lastproductList: MutableState<ProductListState> = mutableStateOf(ProductListState())

}