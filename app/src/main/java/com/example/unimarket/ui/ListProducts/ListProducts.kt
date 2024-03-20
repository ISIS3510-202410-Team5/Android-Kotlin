package com.example.unimarket.ui.ListProducts

import android.content.res.Configuration
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.unimarket.model.Product
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.DockedSearchBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.unimarket.data.Datasource


@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true,
    showSystemUi = true)
@Composable
fun ListProductApp(modifier: Modifier = Modifier) {
    val productList = remember { Datasource().loadAffirmations() }
    val ctx = LocalContext.current
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(true) } // Mantén la barra de búsqueda activa todo el tiempo
    
    Column {

        SearchBar(
            query = query,
            onQueryChange = { query = it },
            onSearch = {
            },
            active = active,
            onActiveChange = { active = it },
        )
        {

            ProductList(
                productList = productList.filter { product ->
                    product.stringResourceId.contains(query, ignoreCase = true)
                }
            )


        }
    }
}


@Composable
fun ProductList(productList: List<Product>, modifier: Modifier = Modifier) {
    LazyColumn(modifier = modifier) {
        items(productList) { product ->
            ProductCard(
                product = product,
                modifier = Modifier.padding(8.dp)
                    .clickable {

                    }
            )

        }
    }
}

@Composable
fun ProductCard(product: Product, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT

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

            if (isPortrait) {
                Box(
                    modifier = Modifier
                        .size(200.dp)
                ) {

                    Text(
                        text = product.stringResourceId,
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
                        contentDescription = product.stringResourceId,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            } else {

                Box(
                    modifier = Modifier
                        .size(370.dp)
                ) {

                    Text(
                        text = product.stringResourceId,
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
                        .size(370.dp)
                ) {
                    Image(
                        painter = painterResource(product.imageResourceId),
                        contentDescription = product.stringResourceId,
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop
                    )
                }
            }
        }
    }
}
