package com.example.unimarket.ui.usuario

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
}
