package com.example.unimarket

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.unimarket.ui.camera.ui.CameraScreen
import com.example.unimarket.ui.camera.ui.CameraViewModel
import com.example.unimarket.ui.camera.ui.LightSensorViewModel
import com.example.unimarket.ui.navigation.Nav
import com.example.unimarket.ui.theme.UniMarketTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    val lightSensorViewModel: LightSensorViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        setContent {
            UniMarketTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    //Greeting("Android")
                    //The viewModel of a screen MUST BE DECLARED OUTSIDE
                    //IF NOT , THE VIEWMODEL WILL BE CREATED AGAIN EACH TIME
                    //THERE IS A CHANGE IN MUTABLES
                    //val homeViewModel = HomeViewModel()
                    //Home(viewModel = homeViewModel)
                    //val shoppingCartViewModel = ShoppingCartViewModel()
                    //ShoppingCart(viewModel = shoppingCartViewModel)
                    //CameraScreen(viewModel = CameraViewModel(), lightSensorViewModel )
                    Nav(lightSensorViewModel)
                    //Greeting("Android")
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    UniMarketTheme {
        Greeting("Android")
    }
}