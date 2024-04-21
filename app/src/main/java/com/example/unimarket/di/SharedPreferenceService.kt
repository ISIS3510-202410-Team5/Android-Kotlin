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
            /* name = */ context.getString(R.string.usersharedpreferences),
            /* mode = */ Context.MODE_PRIVATE
        )
        initPreferences(context)
    }

    private fun initPreferences(context: Context) {
        /* initialize shakeDetector threshold*/
        sharedPreferences.edit().putFloat(context.getString(R.string.shakedetector), 10.0f).apply()

        /* Other values should be initialized using this same notation */
    }


    fun getShakeDetectorThreshold(): Float {
        return sharedPreferences.getFloat(
            /* key = */ "shakeDetector",
            /* defValue = */ -1.0f)
    }

    suspend fun putShakeDetectorThreshold(newThreshold: Float): Unit= withContext(Dispatchers.IO) {
        sharedPreferences.edit().putFloat(
            /* key = */ "shakeDetector",
            /* value = */ newThreshold
        ).apply()
    }
}