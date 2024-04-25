package com.example.unimarket.model

import java.util.Date

data class Purchase(
    val date: Date,
    val user: String,
    val products: String,
)
{
    constructor(): this(Date(),"","")
}

