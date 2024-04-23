package com.example.unimarket.ui

import android.app.Application
import com.example.unimarket.di.SharedPreferenceService
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@HiltAndroidApp
class FirebaseApplication: Application() {




    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            SharedPreferenceService.init(this@FirebaseApplication)
        }

    }
}