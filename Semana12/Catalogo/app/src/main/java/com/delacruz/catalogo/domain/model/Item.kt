package com.delacruz.catalogo.domain.model

/**
 * Modelo que consume la interfaz (capa domain).
 *
 * Está adaptado al tema Productos y a la variante "beauty" de DummyJSON:
 * id, título, marca, descripción, precio, calificación, stock, categoría e imagen.
 * Es independiente del DTO de la API y de la Entity de Room.
 */
data class Item(
    val id: Int,
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
