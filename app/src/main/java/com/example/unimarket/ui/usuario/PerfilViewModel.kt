package com.example.unimarket.ui.usuario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.unimarket.repositories.UsuarioRepository
import kotlinx.coroutines.Dispatchers
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class PerfilViewModel @Inject constructor(
    private val usuarioRepository: UsuarioRepository
) : ViewModel() {

    fun obtenerUsuarioPorCorreo(correo: String) = liveData {
        emit(usuarioRepository.getUsuarioPorCorreo(correo))
    }
}
