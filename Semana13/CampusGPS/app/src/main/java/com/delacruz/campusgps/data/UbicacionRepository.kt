package com.delacruz.campusgps.data

import com.delacruz.campusgps.model.Posicion

interface UbicacionRepository {
    suspend fun obtenerPosicion(): Result<Posicion>
}
