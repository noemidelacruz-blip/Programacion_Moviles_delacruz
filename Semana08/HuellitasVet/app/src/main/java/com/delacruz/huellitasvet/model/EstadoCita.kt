package com.delacruz.huellitasvet.model

// Estados posibles de una cita. ATENDIDA y CANCELADA son estados finales
enum class EstadoCita(val etiqueta: String) {
    PENDIENTE("Pendiente"),
    CONFIRMADA("Confirmada"),
    ATENDIDA("Atendida"),
    CANCELADA("Cancelada");

    // Un estado final ya no puede cambiar
    val esFinal: Boolean
        get() = this == ATENDIDA || this == CANCELADA

    companion object {
        // Convierte el texto guardado en Room de vuelta al enum (PENDIENTE si no coincide)
        fun desdeTexto(texto: String?): EstadoCita =
            entries.firstOrNull { it.name == texto } ?: PENDIENTE
    }
}