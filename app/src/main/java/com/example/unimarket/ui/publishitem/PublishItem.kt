package com.example.unimarket.ui.publishitem

import android.util.Log
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import android.net.Uri
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.unimarket.connection.ConnectivityObserver
import com.example.unimarket.ui.camera.ui.CameraViewModel
import com.example.unimarket.ui.theme.Bittersweet
import com.example.unimarket.ui.theme.GiantsOrange
import com.example.unimarket.ui.theme.Licorice
import com.example.unimarket.ui.theme.UniMarketTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun PublishItem(navController: NavHostController, productUri: String, cameraViewModel: CameraViewModel){

    val viewModel: PublishItemViewModel = hiltViewModel()

    val productTitle by viewModel.productTitle.collectAsState()
    val productPrice by viewModel.productPrice.collectAsState()
    val productCategories by viewModel.productCategories.collectAsState()


    val titleValid: Boolean = productTitle != ""
    val priceValid: Boolean = validPrice(productPrice)
    val categoriesValid: Boolean = validCategories(productCategories)

    val connectStatus by viewModel.connectivityObserver.observe().collectAsState(initial = ConnectivityObserver.Status.Losing)

    /*LaunchedEffect(Unit) {
        withContext(Dispatchers.IO) {
            if (productUri != ""){
                cameraViewModel.uploadImageToFirebase(Uri.parse(productUri))
            }

        }
    }*/


    if (connectStatus != ConnectivityObserver.Status.Available) {
        Fallback()
    } else {

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
        Spacer(modifier = Modifier.height(20.dp))
        CustomTextField(text = productPrice, function = viewModel::onPriceChange, header = "Price")
        Spacer(modifier = Modifier.height(20.dp))
        CustomTextField(text = productCategories, function = viewModel::onCategoriesChange, header = "Categories")
        Spacer(modifier = Modifier.height(30.dp))
        FilledTonalButton(
            onClick = { viewModel.addProductDB(cameraViewModel.imageFirestoreURL.value) },
            enabled = titleValid && priceValid && categoriesValid,
            colors = ButtonDefaults.buttonColors(
                containerColor = GiantsOrange,
                contentColor = Licorice
            )) {
            Text(
                text = "Post"
            )
        }
        Spacer(
            modifier = Modifier.height(30.dp)
        )
        FilledTonalButton(
            onClick = { navController.navigate("CAMERA") },
            colors = ButtonDefaults.buttonColors(
                containerColor = GiantsOrange,
                contentColor = Licorice
            )) {
            Text(
                text = "Take a Picture"
            )
        }


    }}


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
    return try {
        val priceInt = price.toInt()
        priceInt > 0
    } catch (e: Exception){
        false
    }
}

fun validCategories(categories: String) : Boolean {
    return true
}

@Composable
fun Fallback() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Something went wrong!",
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(5.dp),
            fontSize = 30.sp,
            fontFamily = FontFamily.SansSerif,
            color = GiantsOrange,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "You internet connection is Unavailable!",
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(5.dp),
            fontSize = 20.sp,
            fontFamily = FontFamily.SansSerif,
            color = Bittersweet,

        )

    }

}

