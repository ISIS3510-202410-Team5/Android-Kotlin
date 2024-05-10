package com.example.unimarket.ui.SearchProduct

import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.unimarket.R
import com.example.unimarket.di.SharedPreferenceService
import com.example.unimarket.model.Product
import com.example.unimarket.ui.ListProducts.ExampleScreen
import com.example.unimarket.ui.ListProducts.ProductCard
import com.example.unimarket.ui.ListProducts.ProductListState
import com.example.unimarket.ui.navigation.Screen
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun SearchProductApp(modifier: Modifier = Modifier,navController: NavHostController) {

    val viewModel: SearchVideModel = hiltViewModel()
    val state = viewModel.state.value
    val isRefreshing = viewModel.isRefreshing.collectAsState()
    val productList = state.productos
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(true) }

    var isEnabled by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val viewModelUbi: LocationViewModel = hiltViewModel()

    val locationPermissions = rememberMultiplePermissionsState(
        permissions = listOf(
            android.Manifest.permission.ACCESS_COARSE_LOCATION,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        )
    )

    LaunchedEffect(viewModel.isOnline) {
        viewModel.isOnline.collect { isOnline ->
            isEnabled = isOnline
            if (isOnline)
            {
                viewModel.getProductList()
            }
            if (!isOnline) {
                Toast.makeText(context, "No hay conexión. El contenido puede que esté desactualizado.", Toast.LENGTH_LONG).show()
            }
        }

    }

    DisposableEffect(Unit) {
        onDispose {
            viewModel.onClear()
        }
    }

    LaunchedEffect(key1 = locationPermissions.allPermissionsGranted) {
        if (locationPermissions.allPermissionsGranted) {
            viewModelUbi.getCurrentLocation()
        }
    }

    val currentLocation = viewModelUbi.currentLocation

    var isButtonPressed by remember { mutableStateOf(false) }

    val scope = rememberCoroutineScope()

    val filteredProductList = if (isButtonPressed) {
        viewModel.filterUbication(context, currentLocation, query, scope)
            .observeAsState().value
    } else {
        val defaultValue = 0.0

        productList.map { product ->
            if (product.title.contains(query, ignoreCase = true)) {
                SearchVideModel.ProductWithDistance(product, defaultValue)
            } else {
                SearchVideModel.ProductWithDistance(
                    product,
                    0.0
                ) // Aquí podrías especificar un valor predeterminado para la distancia si lo deseas
            }
        }.filter { productWithDistance ->
            productWithDistance.product.title.contains(query, ignoreCase = true)
        }
    }
    AnimatedContent(
        targetState = locationPermissions.allPermissionsGranted, label = ""
    ) { areGranted ->
        if (areGranted) {

            Column {

                TopAppBar(
                    navigationIcon = {
                        IconButton(onClick = { navController.navigate("LIST") }) {
                            Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    },
                    title = {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Spacer(modifier = Modifier.width(8.dp))
                            BasicTextField(
                                value = query,
                                onValueChange = { query = it },
                                textStyle = TextStyle(color = Color.Black),
                                singleLine = true,
                                modifier = Modifier.weight(1f),
                                decorationBox = { innerTextField ->
                                    Box(
                                        contentAlignment = Alignment.CenterStart
                                    ) {
                                        if (query.isEmpty()) {
                                            Text(
                                                text = "Search products ...",
                                                color = Color.Gray
                                            )
                                        }
                                        innerTextField()
                                    }
                                }
                            )
                        }
                    }
                )

                Button(
                    onClick = { isButtonPressed = !isButtonPressed },
                    modifier = Modifier.padding(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isButtonPressed) Color.DarkGray else Color.LightGray // Color del texto en el botón
                    )
                ) {
                    Text(
                        text = "Cercanos",
                    )
                }

                if(isButtonPressed)
                {

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth()
                    ) {
                        var sliderPosition by remember { mutableFloatStateOf(SharedPreferenceService.getLocationThreshold()) }

                        Text(
                            text = "Ajusta como prefieras la distancia de filtrado",
                            modifier = Modifier.fillMaxWidth()
                        )

                        Slider(
                            value = sliderPosition,
                            onValueChange = {
                                sliderPosition = it
                                scope.launch { SharedPreferenceService.putLocationThreshold(sliderPosition) }
                            },
                            steps = 20,
                            valueRange = 0.1f..10f,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Text(
                            text = sliderPosition.toString(),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    filteredProductList?.let {
                        ProductListSearch2(
                            productList = it,
                            modifier = Modifier.weight(1f),
                            isRefreshing = isRefreshing.value,
                            refreshData = viewModel::getProductList,
                            state = state,
                            navController = navController
                        )
                    }

                }else
                {

                    filteredProductList?.let {
                        ProductListSearch1(
                            productList = it,
                            modifier = Modifier.weight(1f),
                            isRefreshing = isRefreshing.value,
                            refreshData = viewModel::getProductList,
                            state = state,
                            navController = navController
                        )
                    }

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

@Composable
fun ProductListSearch1(
    productList: List<SearchVideModel.ProductWithDistance>,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    refreshData: () -> Unit,
    state: ProductListState,
    navController: NavHostController
) {

    SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing), onRefresh = refreshData) {

        LazyColumn(modifier = modifier) {
            items(productList) { product ->
                ProductCardSearch1(
                    product = product,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            navController.navigate(Screen.DetailProduct.route + "/${product.product.id}")
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
                color = Color(0xffff4500)
            )
        )
    }

    if(state.isLoading)
    {
        CircularProgressIndicator()
    }
}

@Composable
fun ProductListSearch2(
    productList: List<SearchVideModel.ProductWithDistance>,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    refreshData: () -> Unit,
    state: ProductListState,
    navController: NavHostController
) {

    SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing), onRefresh = refreshData) {

        LazyColumn(modifier = modifier) {
            items(productList) { product ->
                ProductCardSearch2(
                    product = product,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            navController.navigate(Screen.DetailProduct.route + "/${product.product.id}")
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
                color = Color(0xffff4500)
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
fun ProductCardSearch1(product: SearchVideModel.ProductWithDistance, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val context = LocalContext.current

    var image by remember { mutableStateOf<Drawable?>(null) }

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
                        text = product.product.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xffffcfd5), shape = RoundedCornerShape(4.dp))
                            .padding(8.dp),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xffff4500)
                        )
                    )

                    Text(
                        text = product.product.precio,
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

                Glide.with(context)
                    .load(product.product.coverUrl)
                    .apply(
                        RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.error)
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(object : CustomTarget<Drawable>() {
                        override fun onResourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable>?
                        ) {
                            image = resource
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                        }
                    })

                Image(
                    painter = rememberImagePainter(product.product.coverUrl),
                    contentDescription = product.product.coverUrl,
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

@OptIn(ExperimentalCoilApi::class)
@Composable
fun ProductCardSearch2(product: SearchVideModel.ProductWithDistance, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp

    val context = LocalContext.current

    var image by remember { mutableStateOf<Drawable?>(null) }

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
                        text = product.product.title,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color(0xffffcfd5), shape = RoundedCornerShape(4.dp))
                            .padding(8.dp),
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontWeight = FontWeight.Bold,
                            color = Color(0xffff4500)
                        )
                    )

                    Text(
                        text = product.product.precio,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(7.dp),
                        style = MaterialTheme.typography.bodyMedium.copy(
                            fontWeight = FontWeight.Bold
                        ),
                    )

                    val distance = String.format("%.2f", product.distance).toDouble()

                    Text(
                        text = "Este producto se encuentra a : $distance Km",
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

                Glide.with(context)
                    .load(product.product.coverUrl)
                    .apply(
                        RequestOptions()
                            .centerCrop()
                            .placeholder(R.drawable.placeholder)
                            .error(R.drawable.error)
                            .diskCacheStrategy(DiskCacheStrategy.ALL))
                    .into(object : CustomTarget<Drawable>() {
                        override fun onResourceReady(
                            resource: Drawable,
                            transition: Transition<in Drawable>?
                        ) {
                            image = resource
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {
                        }
                    })

                Image(
                    painter = rememberImagePainter(product.product.coverUrl),
                    contentDescription = product.product.coverUrl,
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