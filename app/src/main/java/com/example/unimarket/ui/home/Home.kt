package com.example.unimarket.ui.home

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorManager
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavHostController
import com.example.unimarket.connection.ConnectivityObserver
import com.example.unimarket.sensor.ShakeDetector
import com.example.unimarket.ui.theme.Bittersweet
import com.example.unimarket.ui.theme.CoolGray
import com.example.unimarket.ui.theme.GiantsOrange
import com.example.unimarket.ui.theme.Licorice
import com.example.unimarket.ui.theme.UniMarketTheme

@Composable
fun Home(navController: NavHostController) {

    val viewModel: HomeViewModel = hiltViewModel()
    val users = viewModel.state.value.users
    var userNumber: Int = users.regUsers

    val scope = rememberCoroutineScope()




    //val searchText by viewModel.searchText.collectAsState()

    //val isSearching by viewModel.isSearching.collectAsState()

    val context = LocalContext.current
    //val lifeCycleOwner = LocalLifecycleOwner.current
    val sensorManager = remember {context.getSystemService(ComponentActivity.SENSOR_SERVICE) as SensorManager}
    val accelSensor = remember { sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)}

    val shakeListener = remember { ShakeDetector(navController, scope)}

    val connectStatus  by viewModel.connectivityObserver.observe().collectAsState(initial = ConnectivityObserver.Status.Losing)

    val catList by viewModel.categories.collectAsState()



    var isEnabled by remember { mutableStateOf(false) }

    LaunchedEffect(viewModel.isOnline) {
        viewModel.isOnline.collect { isOnline ->
            isEnabled = isOnline
            if (isOnline)
            {
                viewModel.getRegUsers()
                viewModel.getCategories()
            }
        }

    }

    DisposableEffect(sensorManager){
        sensorManager.registerListener(shakeListener, accelSensor, SensorManager.SENSOR_DELAY_NORMAL)

        onDispose { sensorManager.unregisterListener(shakeListener)
            }
    }

    Column (
        modifier = Modifier.fillMaxSize()
    ) {
        /*
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
        )*/
        FilledTonalButton(
            onClick= {
                if(isEnabled){

                    navController.navigate("LIST")

                }
                else{

                    Toast.makeText(context, "No hay conexión", Toast.LENGTH_SHORT).show()

                }
                     },
            colors = ButtonDefaults.buttonColors(
                containerColor = GiantsOrange,
                contentColor = Licorice
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
            ){
            Text(
                text = "Search for a product"
            )
        }
        //Log.d("Home", "Test Live data changes $connectStatus")

        when (connectStatus) {
            ConnectivityObserver.Status.Available -> {ShowSales(userNumber)}
            else -> { SalesFallBack() }
        }
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
        if (catList.isNotEmpty()) {
            Column(modifier = Modifier.verticalScroll(rememberScrollState()))
            {
                CatButtonRow(
                    icon1 = catButton.button4.icon,
                    label1 = catList[0].catName,
                    icon2 = catButton.button4.icon,
                    label2 = catList[1].catName,
                    navController = navController
                )
                CatButtonRow(
                    icon1 = catButton.button1.icon,
                    label1 = catList[2].catName,
                    icon2 = catButton.button4.icon,
                    label2 = catList[3].catName,
                    navController = navController
                )
                CatButtonRow(
                    icon1 = catButton.button4.icon,
                    label1 = catList[4].catName,
                    icon2 = catButton.button4.icon,
                    label2 = catList[5].catName,
                    navController = navController
                )
            }
        } else {
            Text(
                text = "No recommended categories found. Connect to network to start browsing",
                modifier = Modifier
                    .align(alignment = Alignment.CenterHorizontally)
                    .padding(10.dp),
                fontSize = 15.sp,
                fontFamily = FontFamily.SansSerif,
                color = GiantsOrange
            )
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
            text="Around: ",
            modifier = Modifier
                .align(alignment = Alignment.Start)
                .padding(5.dp),
            fontSize = 15.sp,
            fontFamily = FontFamily.SansSerif
        )
        Text(
            text="$sales University students",
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
            text="have found what they need in UniMarket",
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
fun CatButton(modifier: Modifier = Modifier, buttonIcon: ImageVector, buttonLabel: String, navController: NavHostController) {
    Column(
        modifier = modifier
            .clickable { navController.navigate("CATEGORY/${buttonLabel}") },
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
fun CatButtonRow(modifier: Modifier = Modifier, icon1: ImageVector?, label1: String?, icon2: ImageVector?, label2: String?, navController: NavHostController){
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 15.dp, vertical = 30.dp)
    ){
        if (icon1 == null || label1 == null){
            Spacer(modifier = Modifier.weight(1f))
        }else {
            CatButton(modifier = Modifier.weight(1f), buttonIcon = icon1, buttonLabel = label1, navController)
        }

        if (icon2 == null || label2 == null){
            Spacer(modifier = Modifier.weight(1f))
        } else
        {
            CatButton(modifier = Modifier.weight(1f), buttonIcon = icon2, buttonLabel = label2, navController)
        }

    }
}


@Composable
fun SalesFallBack(modifier: Modifier = Modifier) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "BEWARE",
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            modifier = modifier
                .align(alignment = Alignment.CenterHorizontally)
                .border(BorderStroke(5.dp, Bittersweet), shape = RoundedCornerShape(8.dp))
                .padding(10.dp),
            color = GiantsOrange,
            fontFamily = FontFamily.SansSerif
        )
        Text(
            text = "You are using the app without connection.",
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            modifier = modifier
                .align(alignment = Alignment.CenterHorizontally)
                .padding(10.dp),
            color = Bittersweet,
            fontFamily = FontFamily.SansSerif
        )
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
