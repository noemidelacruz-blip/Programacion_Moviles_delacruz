package com.delacruz.catalogo.ui

/**
 * Estado que exponen todos los ViewModel de la aplicación.
 *
 * Solo puede estar en uno de estos tres casos, así la pantalla nunca se queda
 * sin saber qué mostrar: cargando, con datos o con error.
 */
sealed interface UiState<out T> {

    /** Mientras se espera la primera respuesta (API o Room). */
    data object Cargando : UiState<Nothing>

    /** Cuando ya hay datos para mostrar (incluye la lista vacía). */
    data class Exito<T>(val datos: T) : UiState<T>

    /** Cuando algo falló y no hay datos guardados para mostrar. */
    data class Error(val mensaje: String) : UiState<Nothing>
}
