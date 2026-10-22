package com.delacruz.huellitasvet.model

// Sexo de la mascota (chips Macho / Hembra del formulario)
enum class Sexo(val etiqueta: String) {
    MACHO("Macho"),
    HEMBRA("Hembra");

    companion object {
        // Convierte el texto guardado en Room de vuelta al enum (MACHO si no coincide)
        fun desdeTexto(texto: String?): Sexo =
            entries.firstOrNull { it.name == texto } ?: MACHO
    }
}