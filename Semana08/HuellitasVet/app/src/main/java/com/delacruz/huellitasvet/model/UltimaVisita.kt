package com.delacruz.huellitasvet.model

// Resultado de la consulta con GROUP BY de Pacientes: la fecha de la ultima atencion de cada mascota.
// Los nombres de los campos deben coincidir con los alias de la consulta SQL (mascotaId y fecha)
data class UltimaVisita(
    val mascotaId: Int,
    val fecha: String
)