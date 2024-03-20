package com.example.unimarket.ui.ListProducts

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.unimarket.model.Product
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unimarket.data.Datasource


@Preview(showBackground = true,
    showSystemUi = true)
@Composable
fun ListProductApp() {
    ProductList(
        productList = Datasource().loadAffirmations(),
    )
}

@Composable
fun ProductList(productList: List<Product>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(productList) { product ->
            AffirmationCard(
                product = product,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
fun AffirmationCard(product: Product, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    Box(
        modifier = modifier
            .padding(1.dp)
            .background(colors.surface)
            .border(1.dp, colors.onSurface, shape = RoundedCornerShape(8.dp))
    )
    {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {

            Box(
                modifier = Modifier
                    .size(200.dp)
            ) {

                Text(
                    text = LocalContext.current.getString(product.stringResourceId),
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color(0xffffcfd5), shape = RoundedCornerShape(4.dp))
                        .padding(8.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        color = Color(0xffff4500) // Color naranja
                    )
                )
            }

            Box(
                modifier = Modifier
                    .size(200.dp)
                    .padding(1.dp)
            ) {
                Image(
                    painter = painterResource(product.imageResourceId),
                    contentDescription = stringResource(product.stringResourceId),
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}
