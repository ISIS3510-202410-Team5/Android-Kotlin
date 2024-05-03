package com.example.unimarket.ui.DetailProduct

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import coil.annotation.ExperimentalCoilApi
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.unimarket.R
import com.example.unimarket.model.Product
import com.example.unimarket.ui.ListProducts.ProductCard
import com.example.unimarket.ui.ListProducts.ProductList
import com.example.unimarket.ui.ListProducts.ProductListState
import com.example.unimarket.ui.ListProducts.ProductListViewModel
import com.example.unimarket.ui.ListProducts.SelectedProductViewModel
import com.example.unimarket.ui.navigation.Screen
import com.example.unimarket.ui.theme.GiantsOrange
import com.example.unimarket.ui.theme.Licorice
import com.google.accompanist.swiperefresh.SwipeRefresh
import com.google.accompanist.swiperefresh.rememberSwipeRefreshState
import com.skydoves.landscapist.rememberDrawablePainter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class, ExperimentalCoilApi::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun DetailProduct(navController: NavHostController, productId: String) {

    val detailviewModel: DetailProductViewModel = hiltViewModel()

    var producto by remember { mutableStateOf<Product?>(null) }

    var image by remember { mutableStateOf<Drawable?>(null) }

    val context = LocalContext.current

    val state = detailviewModel.productosRelacionados.value

    val productList = state.productos

    LaunchedEffect(Unit) {
        detailviewModel.getRelatedProductsView(productId)
        producto = detailviewModel._selectedProduct.value
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

            Glide.with(context)
                .load(it.coverUrl)
                .apply(RequestOptions()
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

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.SpaceEvenly,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = it.title,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(4.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 30.sp,
                        color  = LocalContentColor.current
                    )
                )

                image?.let {
                    Image(
                        painter = rememberDrawablePainter(it),
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(250.dp,250.dp)
                    )
                }

                Divider(
                    color = Color.Black,
                    thickness = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                )

                Text(
                    text = "$ " + it.precio,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                        color  = LocalContentColor.current
                    )
                )

                Row {

                    FilledTonalButton(
                        onClick= { },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = GiantsOrange,
                            contentColor = Licorice
                        ),
                        modifier = Modifier
                            .width(150.dp)
                    ){
                        Text(
                            text = "Buy now"
                        )
                    }

                    FilledTonalButton(
                        onClick= { producto?.let { detailviewModel.addToShoppingCart(it) } },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xffffcfd5),
                            contentColor = Licorice
                        ),
                        modifier = Modifier
                            .width(180.dp)
                    ){
                        Text(
                            text = "Add product to cart"
                        )
                    }

                }

                Text(
                    text = "Related products",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(2.dp),
                    style = MaterialTheme.typography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 25.sp,
                        color  = LocalContentColor.current
                    )
                )

                ProductListDetail(
                    productList = productList,
                    state = state,
                    navController = navController
                )

            }

        }

    }
}


@Composable
fun ProductListDetail(
    productList: List<Product>,
    modifier: Modifier = Modifier,
    state: ProductListState,
    navController: NavHostController
) {

        LazyColumn(modifier = modifier
            .height(150.dp)) {
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