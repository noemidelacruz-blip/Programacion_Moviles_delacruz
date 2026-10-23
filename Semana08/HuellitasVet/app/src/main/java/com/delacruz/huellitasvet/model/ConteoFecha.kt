package com.delacruz.huellitasvet.model

// Resultado de la consulta que cuenta las citas de cada dia en un rango de fechas.
// Los nombres de los campos deben coincidir con los alias de la consulta SQL (fecha y total)
data class ConteoFecha(
    val fecha: String,
    val total: Int
)
