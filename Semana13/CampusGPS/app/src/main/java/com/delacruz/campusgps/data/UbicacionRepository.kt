package com.delacruz.campusgps.data

import com.delacruz.campusgps.model.Posicion
import kotlinx.coroutines.flow.Flow

interface UbicacionRepository {
    // Una sola lectura de la posición (botón "Obtener mi ubicación")
    suspend fun obtenerPosicion(): Result<Posicion>

    // Flujo continuo de posiciones mientras el usuario camina (seguimiento en vivo)
    fun observarUbicacion(): Flow<Posicion>

    fun playServicesDisponible(): Boolean
}
