package com.delacruz.huellitasvet.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.delacruz.huellitasvet.data.local.dao.CitaDao
import com.delacruz.huellitasvet.data.local.dao.MascotaDao
import com.delacruz.huellitasvet.data.local.dao.ServicioDao
import com.delacruz.huellitasvet.data.local.dao.UsuarioDao
import com.delacruz.huellitasvet.data.local.entity.Cita
import com.delacruz.huellitasvet.data.local.entity.Mascota
import com.delacruz.huellitasvet.data.local.entity.Servicio
import com.delacruz.huellitasvet.data.local.entity.Usuario

// Base de datos Room de la app (archivo huellitasvet.db) con las 4 tablas:
// usuarios, servicios, mascotas y citas
@Database(
    entities = [Servicio::class, Usuario::class, Mascota::class, Cita::class],
    version = 1,
    exportSchema = false
)
abstract class VetDatabase : RoomDatabase() {

    abstract fun servicioDao(): ServicioDao
    abstract fun usuarioDao(): UsuarioDao
    abstract fun mascotaDao(): MascotaDao
    abstract fun citaDao(): CitaDao

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