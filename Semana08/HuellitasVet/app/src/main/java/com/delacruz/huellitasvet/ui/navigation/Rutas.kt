package com.delacruz.huellitasvet.ui.navigation

// Nombres de las rutas de navegacion
object Rutas {
    const val SPLASH = "splash"
    const val LOGIN = "login"

    // Dueno
    const val INICIO = "inicio"
    const val MASCOTAS = "mascotas"
    const val CITAS = "citas"
    const val PERFIL = "perfil"

    // Veterinario
    const val PANEL = "panel"
    const val AGENDA = "agenda"
    const val PACIENTES = "pacientes"
    const val SERVICIOS = "servicios"
    const val AJUSTES = "ajustes"

    // Rutas con argumentos: la ficha recibe el id de la mascota y el formulario lo recibe
    // opcionalmente (0 = mascota nueva)
    const val FICHA = "ficha/{id}"
    const val FORMULARIO = "formulario?id={id}"

    // Pedir cita puede abrirse con una mascota ya elegida (0 = ninguna)
    const val PEDIR_CITA = "pedir-cita?mascotaId={mascotaId}"

    // Del veterinario: atender una cita, ver la ficha de un paciente y el formulario de servicio
    const val ATENDER = "atender/{id}"
    const val FICHA_VET = "ficha-vet/{id}"
    const val FORMULARIO_SERVICIO = "formulario-servicio?id={id}"

    fun ficha(id: Int) = "ficha/$id"
    fun formulario(id: Int = 0) = "formulario?id=$id"
    fun pedirCita(mascotaId: Int = 0) = "pedir-cita?mascotaId=$mascotaId"
    fun atender(id: Int) = "atender/$id"
    fun fichaVet(id: Int) = "ficha-vet/$id"
    fun formularioServicio(id: Int = 0) = "formulario-servicio?id=$id"
}