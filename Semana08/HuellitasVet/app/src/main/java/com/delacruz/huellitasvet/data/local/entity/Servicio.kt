package com.delacruz.huellitasvet.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

// Servicio que ofrece la clinica (tabla servicios).
// icono guarda una clave de texto: heart, syringe o scissors
@Entity(tableName = "servicios")
data class Servicio(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val nombre: String,
    val descripcion: String,
    val duracionMin: Int,
    val precio: Double,
    val activo: Boolean = true,
    val icono: String
)