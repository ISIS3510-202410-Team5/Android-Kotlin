package com.example.unimarket.ui.Login.model

import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.tasks.await

class LoginModel {
    // Función para iniciar sesión con Firebase Authentication
    suspend fun signIn(email: String, password: String) {
        try {
            // Iniciar sesión con el correo electrónico y la contraseña utilizando Firebase Authentication
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            // Manejar cualquier excepción que pueda ocurrir durante el inicio de sesión
            throw LoginException("Error al iniciar sesión: ${e.message}")
        }
    }

    // Función para registrar un nuevo usuario con Firebase Authentication
    suspend fun signUp(email: String, password: String) {
        try {
            // Registrar un nuevo usuario con el correo electrónico y la contraseña utilizando Firebase Authentication
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).await()
        } catch (e: Exception) {
            // Manejar cualquier excepción que pueda ocurrir durante el registro
            throw SignUpException("Error al registrar usuario: ${e.message}")
        }
    }

    // Clase de excepción personalizada para manejar errores de inicio de sesión
    class LoginException(message: String) : Exception(message)

    // Clase de excepción personalizada para manejar errores de registro de usuario
    class SignUpException(message: String) : Exception(message)

}