package com.example.unimarket.di

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule
{

    @Provides
    @Singleton
    fun provideFirestoreInstance() = FirebaseFirestore.getInstance()

    @Provides
    @Singleton
    @Named("productos")
    fun provideBookList(firestore: FirebaseFirestore) = firestore.collection("productos")

    @Provides
    @Singleton
    @Named("users")
    fun provideUserList(firestore: FirebaseFirestore) = firestore.collection("users")
}