package com.example.unimarket.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.unimarket.entities.CategoryDAO
import com.example.unimarket.entities.CategoryEntity
import com.example.unimarket.entities.ProductoDAO
import com.example.unimarket.entities.ProductoEntity
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject


@Database(entities = [ProductoEntity::class, CategoryEntity::class], version = 3, exportSchema = false)
public abstract class ShoppingCartDb

    constructor(): RoomDatabase() {

    abstract fun ProductoDAO() : ProductoDAO
    abstract fun CategoryDAO() : CategoryDAO

    companion object {
        //Singleton
        @Volatile
        private var INSTANCE: ShoppingCartDb? = null

        fun getDatabase(context: Context, scope: CoroutineScope) : ShoppingCartDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShoppingCartDb::class.java,
                    "AppDB"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                Log.d("Db Creation", context.getDatabasePath("AppDB").absolutePath)
                // return instance
                instance
            }
        }
    }
}