package com.delacruz.catalogo.data.mapper

import com.delacruz.catalogo.data.remote.dto.ItemDto
import com.delacruz.catalogo.domain.model.Item

/**
 * Conversión DTO (API) -> modelo de la UI.
 *
 * En el hito H9 se agrega también DTO -> Entity -> modelo, porque Room pasa a
 * ser la fuente de verdad.
 */
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
