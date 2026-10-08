package com.delacruz.saludpluscitas.data.model

// profesion es el texto que se muestra bajo el nombre ("Ginecóloga").
data class Medico(
    val id: Int,
    val nombre: String,
    val especialidadId: Int,
    val profesion: String,
    val cmp: String,
    val calificacion: Double,
    val resenas: Int,
    val disponibilidad: String,
    val direccion: String
)