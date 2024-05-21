package com.example.unimarket.di

import android.app.Application
import android.util.Log
import com.example.unimarket.data.DefaultLocationTracker
import com.example.unimarket.data.LocationTracker
import com.example.unimarket.db.ShoppingCartDb
import com.example.unimarket.entities.CategoryDAO
import com.example.unimarket.entities.ProductoDAO
import com.example.unimarket.repositories.ChatRepository
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.FirebaseFirestore
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
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
    @Named("Usuarios")
    fun provideUsuariosList(firestore: FirebaseFirestore) = firestore.collection("Usuarios")

    @Provides
    @Singleton
    @Named("users")
    fun provideUserList(firestore: FirebaseFirestore) = firestore.collection("users")

    @Provides
    @Singleton
    @Named("shoppingCart")
    fun provideShoppingCartCollection(firestore: FirebaseFirestore) = firestore.collection("shoppingCart")

    @Provides
    @Singleton
    @Named("categories")
    fun provideCategoriesCollection(firestore: FirebaseFirestore) = firestore.collection("Categories")

    @Provides
    @Singleton
    fun providesFusedLocationProviderClient(
        application: Application
    ): FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)

    @Provides
    @Singleton
    fun providesLocationTracker(
        fusedLocationProviderClient: FusedLocationProviderClient,
        application: Application
    ): LocationTracker = DefaultLocationTracker(
        fusedLocationProviderClient = fusedLocationProviderClient,
        application = application
    )

    @Singleton
    @Provides
    fun provideProductoDao(shoppingCartDb: ShoppingCartDb) : ProductoDAO {
        return shoppingCartDb.ProductoDAO()
    }

    @Singleton
    @Provides
    fun provideCategoryDao(shoppingCartDb: ShoppingCartDb): CategoryDAO {
        return shoppingCartDb.CategoryDAO()
    }

    @Singleton
    @Provides
    fun provideApplicationScope() : CoroutineScope = CoroutineScope(SupervisorJob())

    @Singleton
    @Provides
    fun provideRoomDbInstance(application: Application, applicationScope: CoroutineScope): ShoppingCartDb
        = ShoppingCartDb.getDatabase(application, applicationScope)

    @Provides
    @Singleton
    fun provideChatRepository(): ChatRepository {
        return ChatRepository()
    }

}