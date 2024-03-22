package com.example.unimarket.ui.publishitem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.unimarket.ui.theme.GiantsOrange
import com.example.unimarket.ui.theme.Licorice
import com.example.unimarket.ui.theme.UniMarketTheme

@Composable
fun PublishItem(navController: NavHostController){

    val viewModel: PublishItemViewModel = hiltViewModel()

    val productTitle by viewModel.productTitle.collectAsState()
    val productPrice by viewModel.productPrice.collectAsState()

    val titleValid: Boolean = productTitle != ""
    val priceValid: Boolean = validPrice(productPrice)

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Text(
            text = "Sell a product",
            fontWeight = FontWeight.Bold,
            fontSize = 30.sp,
            modifier = Modifier
                .align(alignment = Alignment.Start)
                .padding(15.dp)
        )
        Spacer(modifier = Modifier.height(30.dp))
        CustomTextField(text = productTitle, function = viewModel::onTitleChange, header = "Product Title")
        Spacer(
            modifier = Modifier.height(20.dp))
        CustomTextField(text = productPrice, function = viewModel::onPriceChange, header = "Price")
        Spacer(
            modifier = Modifier.height(30.dp)
        )
        FilledTonalButton(
            onClick = { /*TODO*/ },
            enabled = titleValid && priceValid,
            colors = ButtonDefaults.buttonColors(
                containerColor = GiantsOrange,
                contentColor = Licorice
            )) {
            Text(
                text = "Post"
            )
        }



    }


}


@Composable
fun CustomTextField(text: String,function: (String)->Unit, header: String) {
    Column() {
        Text(
            text = header,
            modifier = Modifier.align(Alignment.Start),
            color = GiantsOrange
        )
        TextField(
            value = text,
            onValueChange = { function(it) },
            placeholder = {
                Text(
                    text = ""
                )
            },
            modifier = Modifier.align(Alignment.CenterHorizontally)

        )
    }
}

fun validPrice(price: String): Boolean{
    try {
        price.toInt()
        return true
    } catch (e: Exception){
        return false
    }
}

