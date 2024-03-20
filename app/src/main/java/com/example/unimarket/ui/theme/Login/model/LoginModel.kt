package com.example.unimarket.ui.theme.Login.model

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class LoginModel{
    // Funciones para interactuar con Firebase (signIn, signUp, etc.)
    suspend fun signIn(email: String, password: String) {
        try {
            // Iniciar sesión con el correo electrónico y la contraseña utilizando Firebase Authentication
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            // Manejar cualquier excepción que pueda ocurrir durante el inicio de sesión
            throw LoginException("Error al iniciar sesión: ${e.message}")
        }
    }

    // Clase de excepción personalizada para manejar errores de inicio de sesión
    class LoginException(message: String) : Exception(message)
}
