package com.delacruz.tecsupfit.navigation

import com.delacruz.tecsupfit.model.ClaseFit
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class DestinoFit(val ruta: String, val titulo: String = "") {
    object Inicio : DestinoFit("inicio", "Inicio")
    object Reservas : DestinoFit("reservas", "Reservas")
    object Rutinas : DestinoFit("rutinas", "Rutinas")
    object Perfil : DestinoFit("perfil", "Perfil")

    object DetalleClase : DestinoFit("detalle/{claseId}/{nombre}/{hora}/{sala}") {
        fun crearRuta(clase: ClaseFit): String {
            val nombreEnc = URLEncoder.encode(clase.nombre, StandardCharsets.UTF_8.name())
            val horaEnc = URLEncoder.encode(clase.hora, StandardCharsets.UTF_8.name())
            val salaEnc = URLEncoder.encode(clase.sala, StandardCharsets.UTF_8.name())
            return "detalle/${clase.id}/$nombreEnc/$horaEnc/$salaEnc"
        }
    }

    object Confirmacion : DestinoFit("confirmacion/{nombre}/{hora}/{sala}") {
        fun crearRuta(nombre: String, hora: String, sala: String): String {
            val nombreEnc = URLEncoder.encode(nombre, StandardCharsets.UTF_8.name())
            val horaEnc = URLEncoder.encode(hora, StandardCharsets.UTF_8.name())
            val salaEnc = URLEncoder.encode(sala, StandardCharsets.UTF_8.name())
            return "confirmacion/$nombreEnc/$horaEnc/$salaEnc"
        }
    }
}