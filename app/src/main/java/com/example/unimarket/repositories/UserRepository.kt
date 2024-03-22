package com.example.unimarket.repositories

import com.example.unimarket.model.Product
import com.example.unimarket.model.User
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Named
import javax.inject.Singleton

@Singleton
class UserRepository
@Inject
constructor(
    @Named("users")
    private val userList: CollectionReference)
{
    fun getUserList(): Flow<Result<List<User>>> = flow{
        try {
            emit(Result.Loading<List<User>>())
            val usersList = userList.get().await().map{document -> document.toObject(User::class.java)}
            emit(Result.Success<List<User>>(data = usersList))
        } catch (e: Exception)
        {
            emit(Result.Error<List<User>>(message = e.localizedMessage?: "Unknown Error"))
        }
    }
}