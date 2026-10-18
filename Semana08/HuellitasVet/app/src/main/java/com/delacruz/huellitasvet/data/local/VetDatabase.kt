package com.delacruz.huellitasvet.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.delacruz.huellitasvet.data.local.dao.ServicioDao
import com.delacruz.huellitasvet.data.local.entity.Servicio

// Base de datos Room de la app (archivo huellitasvet.db).
// Por ahora solo tiene la tabla servicios; en el commit 4 se agregan las otras 3
@Database(
    entities = [Servicio::class],
    version = 1,
    exportSchema = false
)
abstract class VetDatabase : RoomDatabase() {

    abstract fun servicioDao(): ServicioDao

    companion object {
        // Una sola instancia de la base para toda la app (singleton)
        @Volatile
        private var INSTANCE: VetDatabase? = null

        fun obtener(context: Context): VetDatabase = INSTANCE ?: synchronized(this) {
            INSTANCE ?: Room.databaseBuilder(
                context.applicationContext,
                VetDatabase::class.java,
                "huellitasvet.db"
            ).build().also { INSTANCE = it }
        }
    }
}