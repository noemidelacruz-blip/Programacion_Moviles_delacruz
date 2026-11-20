package com.delacruz.campusgps.util

import android.location.Location
import com.delacruz.campusgps.model.Lugar
import com.delacruz.campusgps.model.Posicion

//calcula la distancia en metros entre mi ubicación y los lugares del campus y nos da el resulttado en metros.
fun distanciaMetros(desde: Posicion, hasta: Lugar): Float {
    val resultado = FloatArray(1)
    Location.distanceBetween(
        desde.latitud,
        desde.longitud,
        hasta.latitud,
        hasta.longitud,
        resultado
    )
    return resultado[0]
}
