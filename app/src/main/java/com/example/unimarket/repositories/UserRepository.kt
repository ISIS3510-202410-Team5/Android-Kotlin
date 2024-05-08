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
    fun getUserList(): Flow<Result<User>> = flow{
        try {
            emit(Result.Loading<User>())
            val user = userList.document("1").get().await().toObject<User>()
            emit(Result.Success<User>(data = user))
        } catch (e: Exception)
        {
            emit(Result.Error<User>(message = e.localizedMessage?: "Unknown Error"))
        }
    }
}