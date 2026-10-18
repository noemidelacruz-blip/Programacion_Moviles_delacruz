package com.delacruz.huellitasvet.model

// Especie de la mascota. MascotaAvatar dibuja la carita segun este valor
enum class Especie(val etiqueta: String) {
    PERRO("Perro"),
    GATO("Gato"),
    OTRO("Otro");

    companion object {
        // Convierte el texto guardado en Room de vuelta al enum (OTRO si no coincide)
        fun desdeTexto(texto: String?): Especie =
            entries.firstOrNull { it.name == texto } ?: OTRO
    }
}