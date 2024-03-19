package com.example.unimarket.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Build
import androidx.compose.material.icons.rounded.Create
import androidx.compose.material.icons.rounded.Email
import androidx.compose.material.icons.rounded.Face
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.unimarket.ui.theme.Bittersweet
import com.example.unimarket.ui.theme.CoolGray
import com.example.unimarket.ui.theme.GiantsOrange
import com.example.unimarket.ui.theme.UniMarketTheme

@Composable
fun Home(viewModel: HomeViewModel = HomeViewModel()) {


    val searchText by viewModel.searchText.collectAsState()
    //val isSearching by viewModel.isSearching.collectAsState()


    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        TextField(
            value = searchText, //searchText,
            onValueChange = {viewModel.onSearchTextChange(it)}, //viewModel::onSearchTextChange,
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            placeholder = {
                Text(text = "Search")
            },
            leadingIcon = {
                Icon(Icons.Rounded.Search, contentDescription = "Search")
            },
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions( onSearch = {
                viewModel.onToggleSearch()
            })
        )
        ShowSales()
        Divider(color = CoolGray,
            thickness = 5.dp,
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .clip(CircleShape))
        Text(
            text="What are you looking for?",
            fontSize = 25.sp,
            modifier = Modifier.padding(horizontal=15.dp, vertical = 10.dp)
        )
        Divider(color = CoolGray,
            thickness = 5.dp,
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .clip(CircleShape))
        Column(modifier = Modifier.verticalScroll(rememberScrollState()))
        {
            CatButtonRow(icon1 = catButton.button1.icon, label1 = catButton.button1.label,
                icon2 = catButton.button2.icon, label2 = catButton.button2.label)
            CatButtonRow(icon1 = catButton.button3.icon, label1 = catButton.button3.label,
                icon2 = catButton.button4.icon, label2 = catButton.button4.label )
            CatButtonRow(icon1 = catButton.button5.icon, label1 = catButton.button5.label,
                icon2 = catButton.button6.icon, label2 = catButton.button6.label)
        }

    }
}



@Composable
fun ShowSales(sales: Int = 69420){
    Column (
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text="cerca de: ",
            modifier = Modifier
                .align(alignment = Alignment.Start)
                .padding(5.dp),
            fontSize = 15.sp,
            fontFamily = FontFamily.SansSerif
        )
        Text(
            text="$sales personas",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .border(BorderStroke(5.dp, Bittersweet), shape = RoundedCornerShape(8.dp))
                .padding(10.dp),
            color = GiantsOrange,
            fontFamily = FontFamily.SansSerif

        )
        Text(
            text="han encontrado productos en Unimarket",
            modifier = Modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(5.dp),
            fontSize = 15.sp,
            textAlign = TextAlign.Center,
            fontFamily = FontFamily.SansSerif
        )
    }
}

@Composable
fun CatButton(modifier: Modifier = Modifier, buttonIcon: ImageVector, buttonLabel: String) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Box (
            modifier = Modifier
                .border(BorderStroke(5.dp, Bittersweet), shape = RoundedCornerShape(20.dp))
                .size(100.dp)
        ){
            Icon(
                buttonIcon,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp)
            )
        }
        Text(
            text = buttonLabel,
            fontWeight = FontWeight.Bold

        )
    }
}


@Composable
fun CatButtonRow(modifier: Modifier = Modifier, icon1: ImageVector?, label1: String?, icon2: ImageVector?, label2: String?){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 30.dp)
    ){
        if (icon1 == null || label1 == null){
            Spacer(modifier = Modifier.weight(1f))
        }else {
            CatButton(modifier = Modifier.weight(1f), buttonIcon = icon1, buttonLabel = label1)
        }

        if (icon2 == null || label2 == null){
            Spacer(modifier = Modifier.weight(1f))
        } else
        {
            CatButton(modifier = Modifier.weight(1f), buttonIcon = icon2, buttonLabel = label2)
        }

    }
}



//Esto deberia sacarse por logica del viewmodel probablemente

sealed class catButton(val icon: ImageVector?, val label: String?){
    data object button1 : catButton(Icons.Rounded.Create, "Art materials")
    data object button2 : catButton(Icons.Rounded.Face, "Audiovisuals")
    data object button3 : catButton(Icons.Rounded.Build, "Security Elements")
    data object button4: catButton(Icons.Rounded.Email, "Books")
    data object button5: catButton(Icons.Rounded.Settings, "Hardware")
    data object button6: catButton(null, null)
}


@Preview(showBackground = true,
    showSystemUi = true)
@Composable
fun GreetingPreview() {
    UniMarketTheme {
        Home()
    }
}