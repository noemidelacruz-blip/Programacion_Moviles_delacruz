package com.delacruz.centrotareas.data

import kotlinx.coroutines.flow.Flow

data class ProgresoPrimo(val porcentaje: Int, val cantidad: Int)

interface TareasRepository {
    fun contarPrimos(hasta: Int): Flow<ProgresoPrimo>
    suspend fun cargarPerfil(): String
    suspend fun cargarPedidos(simularError: Boolean): List<String>
    val historial: Flow<List<String>>
    suspend fun guardarResultado(resultado: String)
}
