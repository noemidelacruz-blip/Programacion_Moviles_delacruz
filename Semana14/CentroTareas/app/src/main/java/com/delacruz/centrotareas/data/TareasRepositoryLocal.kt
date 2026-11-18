package com.delacruz.centrotareas.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import java.io.IOException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.currentCoroutineContext
import kotlinx.coroutines.delay
import kotlinx.coroutines.ensureActive
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "historial_tareas")

class TareasRepositoryLocal(private val context: Context) : TareasRepository {

    private val keyHistorial = stringPreferencesKey("historial_resultados")

    override val historial: Flow<List<String>> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            val raw = preferences[keyHistorial] ?: ""
            if (raw.isBlank()) emptyList() else raw.split("|")
        }

    override suspend fun guardarResultado(resultado: String) {
        context.dataStore.edit { preferences ->
            val raw = preferences[keyHistorial] ?: ""
            val listaActual = if (raw.isBlank()) emptyList() else raw.split("|")
            val nuevaLista = (listOf(resultado) + listaActual).take(5)
            preferences[keyHistorial] = nuevaLista.joinToString("|")
        }
    }

    override suspend fun cargarPerfil(): String {
        delay(2000)
        return "Emi Zavala"
    }

    override suspend fun cargarPedidos(simularError: Boolean): List<String> {
        delay(3000)
        if (simularError) throw IOException("Sin conexión")
        return listOf("Pedido 101", "Pedido 102", "Pedido 103")
    }

    override fun contarPrimos(hasta: Int): Flow<ProgresoPrimo> = flow {
        var cantidad = 0
        val paso = (hasta / 100).coerceAtLeast(1)
        var ultimoPorcentaje = 0

        for (n in 2..hasta) {
            currentCoroutineContext().ensureActive()
            if (esPrimo(n)) cantidad++

            if (n == hasta || n % paso == 0) {
                val porcentaje = ((n.toLong() * 100L) / hasta).toInt()
                if (porcentaje > ultimoPorcentaje) {
                    ultimoPorcentaje = porcentaje
                    emit(ProgresoPrimo(porcentaje, cantidad))
                }
            }
        }

        if (ultimoPorcentaje < 100) emit(ProgresoPrimo(100, cantidad))
    }.flowOn(Dispatchers.Default)

    private fun esPrimo(n: Int): Boolean {
        var d = 2
        while (d.toLong() * d <= n) {
            if (n % d == 0) return false
            d++
        }
        return true
    }
}
