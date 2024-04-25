package com.example.unimarket.data

import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationAvailability
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

class DefaultLocationTracker(
    private val fusedLocationProviderClient: FusedLocationProviderClient,
    private val application: Application
) : LocationTracker {
    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun getCurrentLocation(): Location?{

        val hasAccessFineLocationPermission = ContextCompat.checkSelfPermission(
            application,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val hasAccessCoarseLocationPermission = ContextCompat.checkSelfPermission(
            application,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        val locationManager = application.getSystemService(
            Context.LOCATION_SERVICE
        ) as LocationManager

        val isGpsEnabled = locationManager
            .isProviderEnabled(LocationManager.NETWORK_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)

        if (!isGpsEnabled && !(hasAccessCoarseLocationPermission || hasAccessFineLocationPermission)) {
            return null
        }

        return suspendCancellableCoroutine { cont ->
            val locationRequest = LocationRequest.create().apply {
                interval = 1000 // Intervalo de actualización de la ubicación en milisegundos (por ejemplo, 10000ms = 10 segundos)
                fastestInterval = 5000 // Intervalo de actualización más rápido en milisegundos
                priority = LocationRequest.PRIORITY_HIGH_ACCURACY // Prioridad de la solicitud de ubicación
            }
            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    super.onLocationResult(locationResult)
                    locationResult?.lastLocation?.let {
                        cont.resume(it) {} // Resume coroutine with location result
                    } ?: cont.resume(null) {} // Resume coroutine with null location result
                    fusedLocationProviderClient.removeLocationUpdates(this) // Remove location updates
                }

                override fun onLocationAvailability(locationAvailability: LocationAvailability) {
                    super.onLocationAvailability(locationAvailability)
                    if (locationAvailability?.isLocationAvailable == false) {
                        cont.resume(null) {} // Resume coroutine with null location result
                        fusedLocationProviderClient.removeLocationUpdates(this) // Remove location updates
                    }
                }
            }
            fusedLocationProviderClient.lastLocation.addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val lastKnownLocation = task.result
                    if (lastKnownLocation != null) {
                        cont.resume(lastKnownLocation) {} // Resume coroutine with location result
                    } else {
                        fusedLocationProviderClient.requestLocationUpdates(
                            locationRequest,
                            locationCallback,
                            Looper.getMainLooper()
                        )
                    }
                } else {
                    cont.resume(null) {} // Resume coroutine with null location result
                }
            }
        }

    }
}