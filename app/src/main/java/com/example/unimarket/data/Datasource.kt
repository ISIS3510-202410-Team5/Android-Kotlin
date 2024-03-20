package com.example.unimarket.data

import com.example.unimarket.R
import com.example.unimarket.model.Product

class Datasource() {
    fun loadAffirmations(): List<Product> {
        return listOf<Product>(
            Product("R.string.affirmation1", R.drawable.image1),
            Product("R.string.affirmation2", R.drawable.image2),
            Product("R.string.affirmation3", R.drawable.image3),
            Product("R.string.affirmation4", R.drawable.image4),
            Product("R.string.affirmation5", R.drawable.image5))
    }
}
