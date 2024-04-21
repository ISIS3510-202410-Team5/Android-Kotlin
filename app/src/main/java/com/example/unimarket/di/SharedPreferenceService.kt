package com.example.unimarket.di

import android.content.Context
import android.content.SharedPreferences
import com.example.unimarket.R
import android.content.res.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object SharedPreferenceService {

    private lateinit var sharedPreferences: SharedPreferences
    
    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences(
            /* name = */ Resources.getSystem().getString(R.string.usersharedpreferences),
            /* mode = */ Context.MODE_PRIVATE
        )
        initPreferences()
    }

    private fun initPreferences() {
        /* initialize shakeDetector threshold*/
        sharedPreferences.edit().putFloat(Resources.getSystem().getString(R.string.shakedetector), 10.0f).apply()

        /* Other values should be initialized using this same notation */
    }


    suspend fun getShakeDetectorThreshold(): Float = withContext(Dispatchers.IO) {
        return@withContext sharedPreferences.getFloat(Resources.getSystem().getString(R.string.shakedetector), -1.0f)
    }

    suspend fun putShakeDetectorThreshold(newThreshold: Float): Unit= withContext(Dispatchers.IO) {
        sharedPreferences.edit().putFloat(
            /* key = */ Resources.getSystem().getString(R.string.shakedetector),
            /* value = */ newThreshold
        )
    }
}