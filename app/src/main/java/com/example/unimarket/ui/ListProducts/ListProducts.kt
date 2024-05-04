package com.example.unimarket.ui.ListProducts

import android.content.res.Configuration
import android.graphics.drawable.Drawable
import android.widget.Toast
import android.util.Log
import androidx.compose.animation.AnimatedContent
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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconToggleButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.DisposableEffect
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
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
import com.example.unimarket.ui.navigation.Screen
import com.example.unimarket.ui.theme.Licorice
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.log
import kotlin.math.sin
import kotlin.math.sqrt


@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ListProductApp(modifier: Modifier = Modifier, navController: NavHostController) {

    val viewModel: ProductListViewModel = hiltViewModel()
    val state = viewModel.state.value
    val isRefreshing = viewModel.isRefreshing.collectAsState()
    val productList = state.productos
    val context = LocalContext.current

    var isEnabled by remember { mutableStateOf(false) }

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

    Column() {

        Box(
            contentAlignment = Alignment.BottomEnd,
            modifier = Modifier.fillMaxWidth()
        ){

            IconButton(
                onClick = { navController.navigate("SEARCH") },
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search),
                    contentDescription = "Search",
                    tint = Licorice
                )
            }

        }

        ProductList(
            productList = productList,
            modifier = Modifier.weight(1f),
            isRefreshing = isRefreshing.value,
            refreshData = viewModel::getProductList,
            state = state,
            viewModel = viewModel,
            navController = navController
        )

    }
}



@Composable
fun ProductList(
    productList: List<Product>,
    modifier: Modifier = Modifier,
    isRefreshing: Boolean,
    refreshData: () -> Unit,
    state: ProductListState,
    viewModel: ProductListViewModel,
    navController: NavHostController
) {
    
    SwipeRefresh(state = rememberSwipeRefreshState(isRefreshing), onRefresh = refreshData) {

        LazyColumn(modifier = modifier) {
            items(productList) { product ->
                ProductCard(
                    product = product,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable {
                            navController.navigate(Screen.DetailProduct.route + "/${product.id}")
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
fun ProductCard(product: Product, modifier: Modifier = Modifier) {
    val colors = MaterialTheme.colorScheme
    val isPortrait = LocalConfiguration.current.orientation == Configuration.ORIENTATION_PORTRAIT
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp

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
                        text = product.title,
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

                Glide.with(context)
                    .load(product.coverUrl)
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

