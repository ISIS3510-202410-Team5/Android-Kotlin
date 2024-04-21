package com.example.unimarket.repositories

import android.util.Log
import com.example.unimarket.di.SharedPreferenceService
import com.example.unimarket.model.UsuarioDTO
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class UsuarioRepository @Inject constructor(
    @Named("Usuarios")
    private val usuariosCollection: CollectionReference,
) {
    fun obtenerDatosUsuario(correo: String): Flow<Result<UsuarioDTO>> = flow {
        try {
            emit(Result.Loading())

            val documento = usuariosCollection.document(correo).get().await()
            if (documento.exists()) {
                val usuario = documento.toObject(UsuarioDTO::class.java)
                emit(Result.Success(data = usuario!!))
            } else {
                emit(Result.Error(message = "Usuario no encontrado"))
            }
        } catch (e: Exception) {
            emit(Result.Error(message = e.localizedMessage ?: "Error desconocido"))
        }
    }


    fun actualizarShakeUsuario(correo: String, newShakeVal: String): Flow<Result<Unit>> = flow {
        try {

            emit(Result.Loading())

            val doc = usuariosCollection.document(correo).get().await()
            if (doc.exists()) {
                usuariosCollection.document(correo).update("shake", newShakeVal)
                emit(Result.Success(data = null))
            } else {
                emit(Result.Error(message = "Usuario no encontrado"))
            }

        } catch (e: Exception) {
            emit(Result.Error(message= e.localizedMessage ?: "Error desconocido"))
        }
    }



    fun getLocalShakeUsuario(): Float {
        return SharedPreferenceService.getShakeDetectorThreshold()
    }


    




}
