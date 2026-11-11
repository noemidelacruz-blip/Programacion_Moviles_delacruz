package com.delacruz.catalogo.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Tabla "items" de Room (hito H8).
 *
 * Guarda los productos descargados para que la app funcione sin internet.
 * Es un modelo distinto al ItemDto (API) y al Item (UI): los mappers se
 * encargan de convertir entre ellos.
 */
@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey val id: Int,
    val titulo: String,
    val marca: String,
    val descripcion: String,
    val precio: Double,
    val calificacion: Double,
    val stock: Int,
    val categoria: String,
    val imagenUrl: String?,
    val esFavorito: Boolean = false
)
