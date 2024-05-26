package com.example.unimarket.ui.categoryList

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.unimarket.connection.ConnectivityObserver
import com.example.unimarket.model.Product
import com.example.unimarket.ui.ListProducts.ProductCard
import com.example.unimarket.ui.navigation.Screen
import com.example.unimarket.ui.theme.Bittersweet
import com.example.unimarket.ui.theme.GiantsOrange
import com.example.unimarket.ui.theme.UniMarketTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@Composable
fun CategoryList(navController: NavHostController, categoryName: String) {

    val viewModel: CategoryViewModel = hiltViewModel()

    val productList by viewModel.productList.collectAsState()
    
    val connectStatus by viewModel.connectivityObserver.observe().collectAsState(initial = ConnectivityObserver.Status.Losing)

    LaunchedEffect(Unit){
        withContext(Dispatchers.IO) {
            Log.d("CategoryList", "LaunchedEffect Invoqued")
            viewModel.assignCategory(categoryName)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    )
    {
        Divider(
            color = GiantsOrange,
            thickness = 5.dp,
            modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 10.dp)
                .clip(CircleShape)
        )
        Text(
            text = "Now showing results for: $categoryName",
            modifier = Modifier
                .align(alignment = Alignment.Start)
                .padding(horizontal = 20.dp),
            fontSize = 35.sp,
            fontFamily = FontFamily.SansSerif
        )
        Divider(
            color = GiantsOrange,
            thickness = 5.dp,
            modifier = Modifier
                .padding(horizontal = 5.dp, vertical = 10.dp)
                .clip(CircleShape)
        )
        if (connectStatus != ConnectivityObserver.Status.Available){
            Text(
                text = "The products being displayed may be not updated since you are navegating without network",
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(5.dp),
                fontSize = 15.sp,
                fontFamily = FontFamily.SansSerif,
                color = Bittersweet,
                textAlign = TextAlign.Center
            )
        }
        LazyColumnProducts(products = productList, navController = navController)
    }

}


@Composable
fun LazyColumnProducts(modifier: Modifier = Modifier, products: List<Product>, navController: NavHostController){
    LazyColumn(
        modifier.fillMaxSize()
    ){
        items(products.size) {
            ProductCard(
                product = products[it],
                modifier = modifier
                    .padding(horizontal = 8.dp)
                    .clickable {
                        navController.navigate(Screen.DetailProduct.route + "/${products[it].id}")
                    })
            if (it != products.size-1) {
                Divider()
            }
        }
    }
}