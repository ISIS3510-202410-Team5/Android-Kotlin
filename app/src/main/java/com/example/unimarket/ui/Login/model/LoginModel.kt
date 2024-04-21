package com.example.unimarket.ui.Login.model

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class LoginModel{
    private val firestore = FirestoreProvider.instance
    private val usuariosCollection = firestore.collection("Usuarios")
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

    suspend fun guardarDatosUsuario(correo:String?,password:String?,nombre: String, carrera: String, semestre: String): Boolean {
        return try {
            /*Crear un objeto Map con los datos del usuario*/
            val datosUsuario = mapOf(
                "correo" to correo,
                "password" to password,
                "nombre" to nombre,
                "carrera" to carrera,
                "semestre" to semestre
            )

            /*Agregar los datos a la colección de usuarios en Firestore*/
            if (correo != null) {
                usuariosCollection.document(correo).set(datosUsuario).await()
            }
            true
        } catch (e: Exception) {
            false
        }
    }

}