package com.example.unimarket.di

import android.content.Context
import android.content.SharedPreferences
import com.example.unimarket.R
import android.content.res.Resources
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
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

        sharedPreferences.edit().putFloat(context.getString(R.string.shakedetector), 15.0f).apply()
        sharedPreferences.edit().putFloat(context.getString(R.string.locationdist), 0.1f).apply()
        sharedPreferences.edit().putString("currentUser", "").apply()

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

    fun getLocationThreshold(): Float {
        return sharedPreferences.getFloat(
            /* key = */ "locationThreshold",
            /* defValue = */ 0.1f)
    }

    suspend fun putLocationThreshold(newThreshold: Float): Unit= withContext(Dispatchers.IO) {
        sharedPreferences.edit().putFloat(
            /* key = */ "locationThreshold",
            /* value = */ newThreshold
        ).apply()
    }

    fun getCurrentUser(): String? {
        return sharedPreferences.getString(
            /* key = */ "currentUser",
            /* defValue = */ "")
    }

    suspend fun putCurrentUser(newThreshold: String): Unit= withContext(Dispatchers.IO) {
        sharedPreferences.edit().putString(
            /* key = */ "currentUser",
            /* value = */ newThreshold
        ).apply()
    }
}