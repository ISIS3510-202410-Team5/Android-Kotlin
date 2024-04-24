package com.example.unimarket.ui

import android.app.Application
import com.example.unimarket.db.ShoppingCartDb
import com.example.unimarket.di.SharedPreferenceService
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

@HiltAndroidApp
class FirebaseApplication: Application() {

    val applicationScope = CoroutineScope(SupervisorJob())

    val cartDb by lazy { ShoppingCartDb.getDatabase(this, applicationScope)}




    override fun onCreate() {
        super.onCreate()
        CoroutineScope(Dispatchers.IO).launch {
            SharedPreferenceService.init(this@FirebaseApplication)
        }

    }



}