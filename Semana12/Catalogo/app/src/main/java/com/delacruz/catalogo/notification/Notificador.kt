package com.delacruz.catalogo.notification

/**
 * Contrato para mostrar notificaciones locales.
 *
 * El ViewModel pide la notificación a través de esta interface, así no recibe
 * Context ni importa clases de la vista.
 *
 * La implementación real (NotificadorAndroid) llega en el hito H15.
 */
interface Notificador {

    /** Muestra una notificación local con título y texto. */
    fun mostrar(titulo: String, texto: String)
}
