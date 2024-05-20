package com.example.unimarket.ui.usuario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unimarket.model.UsuarioDTO
import com.example.unimarket.repositories.UsuarioRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch



class UsuarioViewModel(private val usuarioRepository: UsuarioRepository) : ViewModel() {

    private val _usuarioData = MutableStateFlow<com.example.unimarket.repositories.Result<UsuarioDTO>>(com.example.unimarket.repositories.Result.Loading())
    val usuarioData: StateFlow<com.example.unimarket.repositories.Result<UsuarioDTO>> = _usuarioData
    /*
    fun obtenerDatosUsuario(correo: String) {
        viewModelScope.launch {
            usuarioRepository.obtenerDatosUsuario(correo).collect { result ->
                _usuarioData.value = result
            }
        }
    }*/
}


