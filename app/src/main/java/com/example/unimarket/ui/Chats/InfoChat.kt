package com.example.unimarket.ui.Chats

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalContentColor
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SmallTopAppBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import com.example.unimarket.di.SharedPreferenceService
import com.example.unimarket.ui.DetailProduct.ProductListDetail
import com.example.unimarket.ui.ListProducts.ProductCard
import com.example.unimarket.ui.ListProducts.ProductListViewModel
import com.example.unimarket.ui.navigation.Screen
import com.example.unimarket.ui.usuario.PerfilViewModel
import com.example.unimarket.ui.usuario.ProfileImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InfoChat(
    navController: NavHostController,
    chatId: String,
    chatViewModel: ChatViewModel,
    perfilViewModel: PerfilViewModel
) {
    val chatDetails by chatViewModel.chatDetails.collectAsState()
    val usuarioActual: String? = SharedPreferenceService.getCurrentUser()

    val productChatviewModel: ChatProductViewModel = hiltViewModel()

    LaunchedEffect(chatId) {
        chatViewModel.obtenerChatDetails(chatId)
    }

    Scaffold(
        topBar = {
            SmallTopAppBar(
                title = { Text("Chat Info", style = MaterialTheme.typography.displaySmall) },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(Screen.ChatDetail.route + "/${chatId}") }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.smallTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        content = { paddingValues ->
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        if (usuarioActual != chatDetails!!.emailProveedor) {
                            val usuarioState by perfilViewModel.obtenerUsuarioPorCorreo(chatDetails!!.emailProveedor).observeAsState()
                            usuarioState?.let { usuario ->
                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .align(Alignment.CenterVertically)
                                ) {
                                    ProfileImage(imageUrl = usuario.profileImageUrl!!)
                                }
                            }

                        } else {
                            val usuarioState by perfilViewModel.obtenerUsuarioPorCorreo(chatDetails!!.emailCliente).observeAsState()
                            usuarioState?.let { usuario ->
                                Box(
                                    modifier = Modifier
                                        .size(100.dp)
                                        .align(Alignment.CenterVertically)
                                ) {
                                    ProfileImage(imageUrl = usuario.profileImageUrl!!)
                                }
                            }
                        }
                    }

                    if (usuarioActual != chatDetails!!.emailProveedor)
                    {

                        Text(
                            text = chatDetails!!.emailProveedor,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(2.dp),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 25.sp,
                                color  = LocalContentColor.current
                            )
                        )

                    }
                    else
                    {

                        Text(
                            text = chatDetails!!.emailCliente,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(2.dp),
                            style = MaterialTheme.typography.bodyLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 25.sp,
                                color  = LocalContentColor.current
                            )
                        )

                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    val productoId = chatDetails?.productoId

                    LaunchedEffect(productoId) {
                        if (productoId != null) {
                            productChatviewModel.getProductById(productoId)
                        }
                    }

                    val selectedProduct by productChatviewModel.selectedProduct.observeAsState()

                    Box(modifier = Modifier.width(350.dp).height(150.dp))
                    {

                        selectedProduct?.let { product ->
                            ProductCard(
                                product = product,
                                modifier = Modifier
                                    .padding(8.dp)
                                    .clickable {
                                        navController.navigate(Screen.DetailProduct.route + "/${product.id}")
                                    }
                            )
                        } ?: run {
                            Log.e("Product", "Producto no encontrado")
                            // Manejar el caso donde no se encontró el producto, por ejemplo, mostrar un mensaje
                        }

                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    InfoRow(label = "Productos publicados por el mismo usuario:", value = "")
                    Spacer(modifier = Modifier.height(8.dp))

                    if (usuarioActual != chatDetails!!.emailProveedor)
                    {

                        LaunchedEffect(chatDetails!!.emailProveedor) {
                            productChatviewModel.getProductsByProvider(chatDetails!!.emailProveedor)
                        }

                        val stateproveedor = productChatviewModel.providerProductsState.value

                        val productListroveedor = stateproveedor.productos

                        ProductListDetail(
                            productList = productListroveedor,
                            state = stateproveedor,
                            navController = navController
                        )

                    }
                    else
                    {

                        LaunchedEffect(chatDetails!!.emailCliente) {
                            productChatviewModel.getProductsByProvider(chatDetails!!.emailCliente)
                        }

                        val stateproveedor = productChatviewModel.providerProductsState.value

                        val productListroveedor = stateproveedor.productos

                        ProductListDetail(
                            productList = productListroveedor,
                            state = stateproveedor,
                            navController = navController
                        )

                    }
                }
            }
        }
    )
}

@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium)
        Text(value, style = MaterialTheme.typography.bodyLarge)
    }
}