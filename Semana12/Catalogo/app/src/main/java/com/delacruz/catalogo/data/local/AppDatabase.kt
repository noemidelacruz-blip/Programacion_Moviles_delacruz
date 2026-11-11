package com.delacruz.catalogo.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

/**
 * Base de datos local (hito H8).
 *
 * Se crea una sola vez por proceso: el AppContainer la construye y la comparte.
 */
@Database(
    entities = [ItemEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun itemDao(): ItemDao

    companion object {
        private const val NOMBRE_BD = "catalogo.db"

        @Volatile
        private var instancia: AppDatabase? = null

        /** Devuelve la instancia única de la base de datos. */
        fun obtener(contexto: Context): AppDatabase =
            instancia ?: synchronized(this) {
                instancia ?: Room.databaseBuilder(
                    contexto.applicationContext,
                    AppDatabase::class.java,
                    NOMBRE_BD
                ).build().also { instancia = it }
            }
    }
}
