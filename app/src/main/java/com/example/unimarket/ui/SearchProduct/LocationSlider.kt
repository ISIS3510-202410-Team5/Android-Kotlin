package com.example.unimarket.ui.SearchProduct

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Slider
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.unimarket.di.SharedPreferenceService
import kotlinx.coroutines.launch
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationSlider(modifier: Modifier = Modifier,navController: NavHostController)
{

    var scope = rememberCoroutineScope()

    var sliderPosition by remember { mutableFloatStateOf(SharedPreferenceService.getLocationThreshold()) }

    Column {

        TopAppBar(
            navigationIcon = {
                IconButton(onClick = { navController.navigate("SEARCH") }) {
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
                    Text(text = "configura el rango de distancia de busqueda de tus productos")
                }
            }
        )

        Slider(value = sliderPosition,
            onValueChange = {
                sliderPosition = it
                scope.launch {  SharedPreferenceService.putLocationThreshold(sliderPosition)}
            },
            steps = 20,
            valueRange = 0.1f..10f
        )
        Text(
            text = sliderPosition.toString(),

            )
    }
}