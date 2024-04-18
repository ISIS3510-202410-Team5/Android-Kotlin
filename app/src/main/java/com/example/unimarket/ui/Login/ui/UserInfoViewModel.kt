package com.example.unimarket.ui.Login.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.unimarket.ui.Login.model.LoginModel

class UserInfoViewModel(private val firebaseManager: LoginModel, private val signUpViewModel: SignUpViewModel) : ViewModel() {
    private val _nombre = MutableLiveData<String>()
    val nombre: LiveData<String> = _nombre

    private val _carrera = MutableLiveData<String>()
    val carrera: LiveData<String> = _carrera

    private val _semestre = MutableLiveData<String>()
    val semestre: LiveData<String> = _semestre


    val passwordv=""

    private val _guardadoExitoso = MutableLiveData<Boolean>()
    val guardadoExitoso: LiveData<Boolean> = _guardadoExitoso

    fun actualizarDatosUsuario(nombre: String, carrera: String, semestre: String) {
        _nombre.value = nombre
        _carrera.value = carrera
        _semestre.value = semestre
    }
    fun onInfoChanged(nombre: String,carrera: String,semestre: String){
        _nombre.value= nombre
        _carrera.value= carrera
        _semestre.value= semestre
    }

    /* Método para guardar los datos de usuario */
    suspend fun guardarDatosUsuario() {
        try {
            val (emailValue, passwordValue) = signUpViewModel.obtenerEmailYContraseña()
            val emailv=emailValue
            val passwordv=passwordValue


            _guardadoExitoso.value = firebaseManager.guardarDatosUsuario(
                emailv,
                passwordv,
                _nombre.value ?: "",
                _carrera.value ?: "",
                _semestre.value ?: ""
            )
        } catch (e: Exception) {
            /* Manejar el error si ocurre alguna excepción */

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


