package com.example.unimarket.data

import android.location.Location

interface LocationTracker {
    suspend fun getCurrentLocation(): Location?
}