package com.example.ketekura.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.ketekura.model.Pago
import com.example.ketekura.model.User

@Database(entities = [Pago::class, User::class], version = 2, exportSchema = false) // Añadido User y versión incrementada
public abstract class AppDatabase : RoomDatabase() {

    abstract fun pagoDao(): PagoDao
    abstract fun userDao(): UserDao // Añadido el nuevo DAO

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "ketekura_database"
                )
                .fallbackToDestructiveMigration() // Estrategia de migración para desarrollo
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
