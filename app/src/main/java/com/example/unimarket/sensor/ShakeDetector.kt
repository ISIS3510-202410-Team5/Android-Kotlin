package com.example.unimarket.sensor

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.Log
import androidx.navigation.NavHostController
import com.example.unimarket.di.SharedPreferenceService
import kotlin.math.sqrt


class ShakeDetector
(private val navController: NavHostController) : SensorEventListener{


    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val x = event.values[0]
            val y = event.values[1]
            val z = event.values[2]
            val accel = sqrt((x*x)+(y*y)+(z*z).toDouble())
            if (accel > SharedPreferenceService.getShakeDetectorThreshold())
            {
                navController.navigate(route="POST"){
                    popUpTo(route="Post"){inclusive=true}
                }
            }

        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
        Log.d("TAG_SHAKEDETECTOR","onAccuracyChanged")
    }

}