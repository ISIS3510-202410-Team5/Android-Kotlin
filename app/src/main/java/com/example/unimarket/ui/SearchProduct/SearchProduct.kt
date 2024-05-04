package com.example.unimarket.ui.SearchProduct

import android.content.Context
import android.location.LocationManager
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.unimarket.di.SharedPreferenceService
import com.example.unimarket.ui.ListProducts.ProductList
import com.example.unimarket.ui.ListProducts.ProductListViewModel
import com.example.unimarket.ui.ListProducts.SelectedProductViewModel
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun SearchProductApp(modifier: Modifier = Modifier,navController: NavHostController) {

    val viewModel: ProductListViewModel = hiltViewModel()
    val state = viewModel.state.value
    val isRefreshing = viewModel.isRefreshing.collectAsState()
    val productList = state.productos
    var query by remember { mutableStateOf("") }
    var active by remember { mutableStateOf(true) }

    var isEnabled by remember { mutableStateOf(false) }
    val context = LocalContext.current

    val viewModelUbi: LocationViewModel = hiltViewModel()

    val scope = rememberCoroutineScope()

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

    LaunchedEffect(key1 = locationPermissions.allPermissionsGranted) {
        if (locationPermissions.allPermissionsGranted) {
            viewModelUbi.getCurrentLocation()
        }
    }

    val currentLocation = viewModelUbi.currentLocation

    val locationManager = LocalContext.current.getSystemService(
        Context.LOCATION_SERVICE
    ) as LocationManager

    var isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

    var isButtonPressed by remember { mutableStateOf(false) }

    val filteredProductList = if (isButtonPressed) {

        if(isGpsEnabled) {

            productList.filter { product ->
                val distance = calculateDistance(
                    currentLocation?.latitude ?: 0.0, currentLocation?.longitude ?: 0.0,
                    product.latitud.toDouble(), product.longitud.toDouble()
                )
                product.title.contains(query, ignoreCase = true) && distance <= SharedPreferenceService.getLocationThreshold()
            }

        }

        else{

            productList.filter { product ->
                product.title.contains("dwadawdawdawdwadawd", ignoreCase = true)
            }


        }
    } else {

            productList.filter { product ->
                product.title.contains(query, ignoreCase = true)
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

                    Button(
                        onClick = { navController.navigate("SliderLocation") },
                        modifier = Modifier.padding(16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.DarkGray
                        )
                    ) {
                        Text(
                            text = "Ajustar distancia busqueda",
                        )
                    }

                }


                ProductList(
                    productList = filteredProductList,
                    modifier = Modifier.weight(1f),
                    isRefreshing = isRefreshing.value,
                    refreshData = viewModel::getProductList,
                    state = state,
                    viewModel = viewModel,
                    navController = navController
                )

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