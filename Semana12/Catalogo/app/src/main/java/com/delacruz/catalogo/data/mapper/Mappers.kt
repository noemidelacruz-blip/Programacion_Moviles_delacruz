package com.delacruz.catalogo.data.mapper

import com.delacruz.catalogo.data.local.ItemEntity
import com.delacruz.catalogo.data.remote.dto.ItemDto
import com.delacruz.catalogo.domain.model.Item

/**
 * Conversiones entre los tres modelos del proyecto (hito H9):
 *
 * ItemDto (API)  ->  ItemEntity (Room)  ->  Item (UI)
 */
fun ItemDto.aEntity(esFavorito: Boolean = false): ItemEntity = ItemEntity(
    id = id,
    titulo = titulo,
    marca = marca ?: "Sin marca",
    descripcion = descripcion,
    precio = precio,
    calificacion = calificacion,
    stock = stock,
    categoria = categoria,
    imagenUrl = imagenUrl,
    esFavorito = esFavorito
)

fun ItemEntity.aModelo(): Item = Item(
    id = id,
    titulo = titulo,
    marca = marca,
    descripcion = descripcion,
    precio = precio,
    calificacion = calificacion,
    stock = stock,
    categoria = categoria,
    imagenUrl = imagenUrl,
    esFavorito = esFavorito
)

/** Sigue disponible para el detalle, que viene directo de la API. */
fun ItemDto.aModelo(esFavorito: Boolean = false): Item = Item(
    id = id,
    titulo = titulo,
    marca = marca ?: "Sin marca",
    descripcion = descripcion,
    precio = precio,
    calificacion = calificacion,
    stock = stock,
    categoria = categoria,
    imagenUrl = imagenUrl,
    esFavorito = esFavorito
)
