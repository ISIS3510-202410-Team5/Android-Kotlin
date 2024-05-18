package com.example.unimarket.ui.usuario

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unimarket.model.UsuarioDTO
import com.example.unimarket.repositories.UsuarioRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class UsuarioViewModel
@Inject
    constructor(private val usuarioRepository: UsuarioRepository) : ViewModel() {

    private val _usuarioData = MutableStateFlow<com.example.unimarket.repositories.Result<UsuarioDTO>>(com.example.unimarket.repositories.Result.Loading())
    val usuarioData: StateFlow<com.example.unimarket.repositories.Result<UsuarioDTO>> = _usuarioData

    var correoUsuario: String = ""

    fun obtenerDatosUsuario(correo: String) {
        viewModelScope.launch {
            usuarioRepository.obtenerDatosUsuario(correo).collect { result ->
                _usuarioData.value = result
            }
        }
    }

    fun setCorreoUsuarioApp(correo: String)
    {

        correoUsuario = correo

    }

    fun getCorreoUsuarioApp(): String
    {

        return correoUsuario

    }
}



