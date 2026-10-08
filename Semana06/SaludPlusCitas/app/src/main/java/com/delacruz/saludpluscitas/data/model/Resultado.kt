package com.delacruz.saludpluscitas.data.model

// Resultado de un examen o análisis. fecha en formato ISO ("2026-09-18").
// valor es lo medido ("118/76 mmHg"), referencia es el rango esperado
// y estado indica si está dentro del rango ("Normal") o debe revisarse.
data class Resultado(
    val id: Int,
    val titulo: String,
    val especialidad: String,
    val fecha: String,
    val valor: String,
    val referencia: String,
    val estado: String
)