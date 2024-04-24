package com.example.unimarket.db

import android.content.Context
import android.util.Log
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.unimarket.entities.ProductoDAO
import com.example.unimarket.entities.ProductoEntity
import kotlinx.coroutines.CoroutineScope
import javax.inject.Inject


@Database(entities = arrayOf(ProductoEntity::class), version = 1, exportSchema = false)
public abstract class ShoppingCartDb

    constructor(): RoomDatabase() {

    abstract fun ProductoDAO() : ProductoDAO

    companion object {
        //Singleton
        @Volatile
        private var INSTANCE: ShoppingCartDb? = null

        fun getDatabase(context: Context, scope: CoroutineScope) : ShoppingCartDb {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    ShoppingCartDb::class.java,
                    "shopping_cartdb"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                Log.d("Db Creation", context.getDatabasePath("shopping_cartdb").absolutePath)
                // return instance
                instance
            }
        }
    }
}