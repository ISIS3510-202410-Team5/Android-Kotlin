package com.example.unimarket

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.unimarket.ui.theme.Login.model.LoginModel
import com.example.unimarket.ui.theme.Login.ui.LoginScreen
import com.example.unimarket.ui.theme.Login.ui.LoginViewModel
import com.example.unimarket.ui.theme.theme.UniMarketTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val firebaseManager = LoginModel()
        super.onCreate(savedInstanceState)
        setContent {
            UniMarketTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    /*Se crea una instancia con el Login View Model*/
                    LoginScreen(LoginViewModel(firebaseManager))
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