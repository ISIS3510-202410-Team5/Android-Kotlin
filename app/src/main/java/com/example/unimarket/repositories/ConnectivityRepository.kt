package com.example.unimarket.repositories

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.util.Log
import android.widget.Toast
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

class ConnectivityRepository
@Inject
    constructor(private val application: Application) {

    private val connectivityManager =
        application.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private val _isConnected = MutableStateFlow(false)
    val isConnected: Flow<Boolean> = _isConnected

    init {
        // Observe network connectivity changes
        connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: android.net.Network) {
                super.onAvailable(network)
                _isConnected.value = true
            }

            override fun onLost(network: android.net.Network) {
                super.onLost(network)
                _isConnected.value = false
            }
        })
    }
}