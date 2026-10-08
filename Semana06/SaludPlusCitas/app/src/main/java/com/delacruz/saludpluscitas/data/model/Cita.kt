package com.delacruz.saludpluscitas.data.model

// fecha en formato ISO ("2026-10-07") y hora de inicio ("09:30").
// Cada cita dura 30 minutos.
data class Cita(
    val id: Int,
    val usuarioId: Int,
    val medicoId: Int,
    val fecha: String,
    val hora: String,
    val tipo: String = "Consulta presencial",
    val motivo: String = ""
)