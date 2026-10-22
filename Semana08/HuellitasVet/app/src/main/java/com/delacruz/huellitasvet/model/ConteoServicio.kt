package com.delacruz.huellitasvet.model

// Resultado de la consulta con JOIN y GROUP BY del panel: cuantas citas tiene cada servicio.
// Los nombres de los campos deben coincidir con los alias de la consulta SQL (nombre y total)
data class ConteoServicio(
    val nombre: String,
    val total: Int
)