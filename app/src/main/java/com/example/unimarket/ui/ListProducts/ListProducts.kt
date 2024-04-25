package com.example.unimarket.ui.ListProducts

import android.content.res.Configuration
import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.unimarket.model.Product
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.example.unimarket.R
import com.example.unimarket.ui.SearchProduct.LocationViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ListProductApp(modifier: Modifier = Modifier, ) {

    val viewModel: ProductListViewModel = hiltViewModel()
    val state = viewModel.state.value
    val isRefreshing = viewModel.isRefreshing.collectAsState()
    val productList = state.productos
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(true) }

    val viewModelUbi: LocationViewModel = hiltViewModel()

    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    LaunchedEffect(key1 = locationPermissions.allPermissionsGranted) {
        if (locationPermissions.allPermissionsGranted) {
            viewModelUbi.getCurrentLocation()
        }
    }

    val currentLocation = viewModelUbi.currentLocation

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        AnimatedContent(
            targetState = locationPermissions.allPermissionsGranted, label = ""
        ) { areGranted ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (areGranted) {

                    Column() {
                        SearchBar(
                            query = query,
                            onQueryChange = { query = it },
                            onSearch = {},
                            active = active,
                            onActiveChange = { active = it }
                        )
                        {

                            ProductList(
                                productList = productList.filter { product ->
                                    val distance = calculateDistance(
                                        currentLocation?.latitude ?: 0.0, currentLocation?.longitude ?: 0.0,
                                        product.latitud.toDouble(), product.longitud.toDouble()
                                    )
                                    product.title.contains(query, ignoreCase = true) && distance <= 0.4

                                },
                                modifier = Modifier.weight(1f),
                                isRefreshing = isRefreshing.value,
                                refreshData = viewModel::getProductList,
                                state = state,
                                viewModel = viewModel
                            )

                        }

                    }

                } else {
                    Text(text = "We need location permissions for this application.")
                    Button(
                        onClick = { locationPermissions.launchMultiplePermissionRequest() }
                    ) {
                        Text(text = "Accept")
                    }
                }
            }
        }
    }

}



@Composable
fun ProductList(productList: List<Product>, modifier: Modifier = Modifier, isRefreshing: Boolean, refreshData: () -> Unit, state: ProductListState,
                viewModel: ProductListViewModel) {
    
    SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing), onRefresh = refreshData) {

        LazyColumn(modifier = modifier) {
            items(productList) { product ->
                ProductCard(
                    product = product,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            viewModel.addToShoppingCart(product)
                        }
                )

            }
        }

    }

    if(state.error.isBlank())
    {
        Text(
            text = state.error,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            style = MaterialTheme.typography.bodyLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Color(0xffff4500) // Color naranja
            )
        )
    }

    if(state.isLoading)
    {
        CircularProgressIndicator()
    }
}

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ProductCard(product: Product, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

    Box(
        modifier = modifier
            .padding(1.dp)
            .background(colors.surface)
            .border(1.dp, colors.onSurface, shape = RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = if (isPortrait) Modifier.weight(1f) else Modifier.width(screenWidth / 2)
            ) {
                Column {
                    Text(
                        text = product.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xffffcfd5), shape = RoundedCornerShape(4.dp))
                            .padding(8.dp),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xffff4500) // Color naranja
                        )
                    )

                    Text(
                        text = product.precio,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(7.dp),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                    )

                    ExampleScreen()
                }
            }

            Box(
                modifier = if (isPortrait) Modifier.weight(1f) else Modifier
                    .width(screenWidth / 2)
                    .padding(1.dp)
            ) {
                Image(
                    painter = rememberImagePainter(product.coverUrl),
                    contentDescription = product.coverUrl,
                    modifier = Modifier
                        .width((screenWidth / 2) - 1.dp)
                        .height((screenWidth / 2) - 1.dp)
                        .padding(8.dp),
                    contentScale = ContentScale.Crop
                )
            }
        }
    }
}


@Composable
fun ExampleScreen() {
    Column(
        modifier = Modifier.fillMaxWidth()
    ) {
        var checked by remember { mutableStateOf(false) } //1

        IconToggleButton(checked = checked, onCheckedChange = { checked = it }) { //2
            Icon(
                painter = painterResource( //3
                    if (checked) R.drawable.heart_icon
                    else R.drawable.heart_icon_border
                ),
                contentDescription = //4
                if (checked) "Añadir a marcadores"
                else "Quitar de marcadores"//5
            )
        }
    }
}

fun calculateDistance(
    lat1: Double,
    lon1: Double,
    lat2: Double,
    lon2: Double
): Double {
    val radius = 6371 // Radio de la Tierra en kilómetros
    val dLat = Math.toRadians(lat2 - lat1)
    val dLon = Math.toRadians(lon2 - lon1)
    val a = sin(dLat / 2) * sin(dLat / 2) +
            cos(Math.toRadians(lat1)) * cos(Math.toRadians(lat2)) *
            sin(dLon / 2) * sin(dLon / 2)
    val c = 2 * atan2(sqrt(a), sqrt(1 - a))
    return radius * c
}

