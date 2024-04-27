package com.example.unimarket.ui

import android.app.Application
import com.example.unimarket.db.ShoppingCartDb
import com.example.unimarket.di.SharedPreferenceService
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.firestore.firestoreSettings
import com.google.firebase.firestore.ktx.memoryCacheSettings
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
        CoroutineScope(Dispatchers.Default).launch {
            val firestore = FirebaseFirestore.getInstance()
            firestore.firestoreSettings = FirebaseFirestoreSettings.Builder()
                .setLocalCacheSettings(memoryCacheSettings {  })
                .build()

        }

    }



}