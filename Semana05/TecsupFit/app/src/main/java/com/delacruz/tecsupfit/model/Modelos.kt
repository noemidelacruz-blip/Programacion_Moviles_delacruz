package com.delacruz.tecsupfit.model

data class ClaseFit(
    val id: Int,
    val nombre: String,
    val hora: String,
    val sala: String,
    val duracion: String = "45 min",
    val descripcion: String = "Entrenamiento funcional de alta intensidad. Cupos limitados.",
    val cuposDisponibles: Int = 8,
    val cuposTotales: Int = 12
)

data class Reserva(
    val id: Int,
    val claseNombre: String,
    val horario: String,
    val estado: String
)

data class PerfilUsuario(
    val iniciales: String = "DR",
    val nombre: String = "Diego Ramos",
    val plan: String = "Plan Premium",
    val clasesTomadas: Int = 14,
    val rachas: Int = 3
)