package com.delacruz.huellitasvet.model

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

// Genera los horarios de atencion de la clinica: 18 turnos de 09:00 a 17:30, cada 30 minutos
object Horarios {

    private val INICIO: LocalTime = LocalTime.of(9, 0)
    private const val CANTIDAD = 18
    private const val INTERVALO_MIN = 30L
    private val FORMATO: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    // Lista de horas en texto "HH:mm" (el mismo formato que se guarda en la tabla citas)
    val todos: List<String> = List(CANTIDAD) { indice ->
        INICIO.plusMinutes(INTERVALO_MIN * indice).format(FORMATO)
    }

    // Indica si una fecha y hora ya pasaron (se usa para deshabilitar horarios de hoy)
    fun esPasado(fecha: String, hora: String): Boolean {
        val fechaHora = LocalDateTime.of(LocalDate.parse(fecha), LocalTime.parse(hora))
        return fechaHora.isBefore(LocalDateTime.now())
    }
}