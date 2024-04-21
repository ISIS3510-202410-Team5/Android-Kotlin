package com.example.unimarket.model

data class UsuarioDTO(
    val correo: String = "",
    val nombre: String = "",
    val carrera: String = "",
    val semestre: String = ""
)
{
    constructor(): this("","","","")
}