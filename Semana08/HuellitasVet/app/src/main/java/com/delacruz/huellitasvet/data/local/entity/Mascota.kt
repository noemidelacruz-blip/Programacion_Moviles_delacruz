package com.delacruz.huellitasvet.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

// Mascota de un dueno (tabla mascotas). Muchas mascotas pertenecen a un usuario (duenoId).
// especie y sexo se guardan como texto con el name del enum (Especie.name y Sexo.name)
@Entity(
    tableName = "mascotas",
    foreignKeys = [
        ForeignKey(
            entity = Usuario::class,
            parentColumns = ["id"],
            childColumns = ["duenoId"]
        )
    ],
    // Cada columna FK lleva su indice para que las consultas y los JOIN sean rapidos
    indices = [Index("duenoId")]
)
data class Mascota(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val duenoId: Int,
    val nombre: String,
    val especie: String,
    val raza: String,
    val anioNacimiento: Int,
    val peso: Double,
    val sexo: String
)