package com.delacruz.catalogo.notification

/**
 * Contrato de notificaciones (hito H15).
 *
 * El ViewModel depende de esta interface y NO de Android: así no recibe
 * Context ni importa clases de vista (regla MVVM de la rúbrica). La
 * implementación real es NotificadorAndroid.
 */
interface Notificador {

    /** Muestra una notificación local. */
    fun mostrar(titulo: String, texto: String)
}
