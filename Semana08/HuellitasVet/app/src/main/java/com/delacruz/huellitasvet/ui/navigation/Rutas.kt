package com.delacruz.huellitasvet.ui.navigation

// Nombres de las rutas de navegacion. Las demas se agregan en los commits siguientes
object Rutas {
    const val SPLASH = "splash"
    const val LOGIN = "login"
    const val INICIO = "inicio"
    const val MASCOTAS = "mascotas"
    const val CITAS = "citas"
    const val PERFIL = "perfil"
    const val PANEL = "panel"
    const val SERVICIOS = "servicios"

    // Rutas con argumentos: la ficha recibe el id de la mascota y el formulario lo recibe
    // opcionalmente (0 = mascota nueva)
    const val FICHA = "ficha/{id}"
    const val FORMULARIO = "formulario?id={id}"

    fun ficha(id: Int) = "ficha/$id"
    fun formulario(id: Int = 0) = "formulario?id=$id"
}