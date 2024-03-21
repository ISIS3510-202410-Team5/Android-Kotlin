package com.example.unimarket.ui.shoppingcart

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.unimarket.R
import com.example.unimarket.ui.theme.AlmostWhite
import com.example.unimarket.ui.theme.CoolGray
import com.example.unimarket.ui.theme.GiantsOrange
import com.example.unimarket.ui.theme.Licorice
import com.example.unimarket.ui.theme.UniMarketTheme

@Composable
fun ShoppingCart(viewModel: ShoppingCartViewModel = ShoppingCartViewModel(), navController: NavHostController )  {
    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        TotalComposable(total = null, onClickFun = {
            Log.d(null, "PaymentButton pressed")
            //viewModel.pressPayment()
        })
        Divider(color = CoolGray,
            thickness = 3.dp,
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .clip(CircleShape))
        LazyColumn (
            modifier = Modifier
                .padding(top = 10.dp)
                .background(color = AlmostWhite)
        ){
            items(10) { index ->
                Log.d(null, index.toString())
                ProductCard(modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .height(IntrinsicSize.Max),
                    index = index,
                    product = "hello",
                    viewModel = viewModel)
                if (index != 9){
                    Divider(
                        color = CoolGray,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }
    }
}


@Composable
fun TotalComposable(total:Int?, onClickFun: () -> Unit){

    val displayTotal = total?:0

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Total",
            fontWeight = FontWeight.Bold,
            fontSize = 26.sp,
            modifier = Modifier
                .align(alignment = Alignment.Start)
        )
        Text(
            text = "\$$displayTotal COP",
            fontSize = 26.sp,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
        )


            FilledTonalButton(
                onClick = { onClickFun() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 15.dp, vertical = 10.dp)
                    .align(alignment = Alignment.CenterHorizontally),
                colors = ButtonDefaults.buttonColors(
                    containerColor = GiantsOrange,
                    contentColor = Licorice
                )
            ) {
                Text(
                    text = "Payment"
                )
            }

    }
}

@Composable
fun ProductCard(modifier:Modifier = Modifier, index:Int, product: Any, viewModel: ShoppingCartViewModel)
{
    Row(
        modifier = modifier
            .padding(vertical = 10.dp)
            .fillMaxSize(),
        horizontalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background),
                contentDescription = null,
                modifier = Modifier.size(125.dp))
            Spacer(modifier = Modifier.height(10.dp))
            IconButton(
                onClick = { Log.d(null, "press removeButton from $index button") },
                modifier = Modifier
                    .border(width = 2.dp, color = GiantsOrange, shape = CircleShape)
            ) {
                Icon(imageVector = Icons.Rounded.Delete,
                    contentDescription = "delete button",
                    tint = GiantsOrange)
            }
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .weight(1f)
                .padding(end = 10.dp),
            verticalArrangement = Arrangement.SpaceBetween

        ){
            Text(
                text = "Product Name algo",
                modifier = Modifier
                    .align(Alignment.Start)
                    .padding(end = 20.dp),
                fontWeight = FontWeight.Bold,
                fontSize = 23.sp
            )
            Text(
                text = "Product Price",
                modifier = Modifier.align(Alignment.CenterHorizontally),
                fontSize = 19.sp

            )
            Text(
                text = "sold by: Product seller",
                modifier = Modifier.align(Alignment.End),
                fontSize = 14.sp
            )
        }
    }
}
