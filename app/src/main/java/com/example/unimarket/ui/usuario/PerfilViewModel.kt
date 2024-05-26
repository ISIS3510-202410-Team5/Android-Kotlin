package com.example.unimarket.ui.usuario

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.example.unimarket.repositories.UsuarioRepository
import kotlinx.coroutines.Dispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    fun obtenerUsuarioPorCorreo(correo: String) = liveData {
        emit(usuarioRepository.getUsuarioPorCorreo(correo))
    }
    fun actualizarUsuario(correo: String, nuevosDatos: Map<String, Any>) {
        viewModelScope.launch {
            val resultado = usuarioRepository.actualizarUsuario(correo, nuevosDatos)
            if (resultado) {
                obtenerUsuarioPorCorreo(correo)
            } else {

            }
        }
    }
    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val network = connectivityManager.activeNetwork
        val capabilities = connectivityManager.getNetworkCapabilities(network)
        return capabilities != null && (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
    }
}
