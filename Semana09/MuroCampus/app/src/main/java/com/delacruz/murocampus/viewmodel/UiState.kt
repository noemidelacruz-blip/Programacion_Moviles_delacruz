package com.delacruz.murocampus.viewmodel

import kotlinx.serialization.SerializationException
import retrofit2.HttpException
import java.io.IOException

/**
 * Estado de una pantalla que depende de la red. La vista solo dibuja este estado.
 */
sealed interface UiState<out T> {
    data object Cargando : UiState<Nothing>
    data class Exito<T>(val datos: T) : UiState<T>
    data class Error(val mensaje: String) : UiState<Nothing>
}

/**
 * Traduce una excepción de la capa de datos a un mensaje para el usuario.
 * Solo se usa en el ViewModel; la pantalla nunca ve estos tipos.
 */
fun mensajeDe(e: Throwable): String = when (e) {
    is IOException -> "Sin conexión a internet.\nRevisa tu red e inténtalo otra vez."
    is HttpException -> "Error del servidor (${e.code()})"
    is SerializationException -> "Respuesta inesperada"
    else -> "Error desconocido"
}
