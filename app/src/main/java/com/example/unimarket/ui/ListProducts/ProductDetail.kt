package com.example.unimarket.ui.ListProducts

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.unimarket.model.Product
import com.example.unimarket.ui.theme.GiantsOrange
import com.example.unimarket.ui.theme.Licorice
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun DetailProduct(productoId: String, navController: NavHostController) {

    val detailviewModel: DetailProductViewModel = hiltViewModel()

    var producto by remember { mutableStateOf<Product?>(null) }

    LaunchedEffect(Unit) {
        producto = withContext(Dispatchers.Default) {
            detailviewModel.buscarProductoEnLista(productoId)
        }
    }


    Column() {

        TopAppBar(
            navigationIcon = {
                IconButton(onClick = { navController.navigate("LIST") }) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            title = {
            }
        )

        producto?.let {
            Text(
                text = it.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                    color = Color(0xffff4500)
                )
            )
        }

        FilledTonalButton(
            onClick= { producto?.let { detailviewModel.addToShoppingCart(it) } },
            colors = ButtonDefaults.buttonColors(
                containerColor = GiantsOrange,
                contentColor = Licorice
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ){
            Text(
                text = "Add product to cart"
            )
        }
    }
}