package com.example.unimarket.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class Product(
    @StringRes val stringResourceId: Int,
    @DrawableRes val imageResourceId: Int
)
