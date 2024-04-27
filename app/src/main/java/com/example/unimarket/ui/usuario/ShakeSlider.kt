package com.example.unimarket.ui.usuario

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.material3.Slider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.unimarket.di.SharedPreferenceService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun shakeSlider() {

    var scope = rememberCoroutineScope()

    var sliderPosition by remember { mutableFloatStateOf(SharedPreferenceService.getShakeDetectorThreshold()) }
    Column(
        modifier = Modifier
            .padding(15.dp)
    ) {
        Slider(value = sliderPosition,
            onValueChange = {
                sliderPosition = it
                scope.launch {  SharedPreferenceService.putShakeDetectorThreshold(sliderPosition)}
            },
            steps = 10,
            valueRange = 15f..30f
        )
        Text(
            text = sliderPosition.toString(),

        )
    }
}


