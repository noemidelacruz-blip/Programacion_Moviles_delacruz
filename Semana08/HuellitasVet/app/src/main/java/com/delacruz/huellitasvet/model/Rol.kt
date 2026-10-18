package com.delacruz.huellitasvet.model

// Perfil con el que se usa la app. En la base de datos se guarda el nombre (CLIENTE o ADMIN)
enum class Rol(val etiqueta: String) {
    CLIENTE("Dueño"),
    ADMIN("Veterinario");

    companion object {
        // Convierte el texto guardado en Room de vuelta al enum (CLIENTE si no coincide)
        fun desdeTexto(texto: String?): Rol =
            entries.firstOrNull { it.name == texto } ?: CLIENTE
    }
}