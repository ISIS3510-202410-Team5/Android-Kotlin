package com.example.unimarket.ui

import android.app.Application
import com.example.unimarket.di.SharedPreferenceService
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class FirebaseApplication: Application() {




    override fun onCreate() {
        super.onCreate()
        SharedPreferenceService.init(this)

    }
}