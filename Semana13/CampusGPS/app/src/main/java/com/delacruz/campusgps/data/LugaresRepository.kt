package com.delacruz.campusgps.data

import com.delacruz.campusgps.model.Lugar

interface LugaresRepository {
    fun obtenerLugares(): List<Lugar>
}
