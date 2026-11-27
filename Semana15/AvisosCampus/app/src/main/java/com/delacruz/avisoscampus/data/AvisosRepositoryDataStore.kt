package com.delacruz.avisoscampus.data

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import com.delacruz.avisoscampus.model.Aviso
import com.delacruz.avisoscampus.model.Origen
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.json.JSONArray
import org.json.JSONObject

class AvisosRepositoryDataStore(
    private val dataStore: DataStore<Preferences>
) : AvisosRepository {

    private val _avisos = MutableStateFlow<List<Aviso>>(emptyList())
    override val avisos: StateFlow<List<Aviso>> = _avisos.asStateFlow()

    private var siguiente = 1
    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        scope.launch {
            try {
                val prefs = dataStore.data.first()
                val jsonString = prefs[KEY_HISTORIAL]
                if (!jsonString.isNullOrEmpty()) {
                    val array = JSONArray(jsonString)
                    val guardados = mutableListOf<Aviso>()
                    for (i in 0 until array.length()) {
                        val obj = array.getJSONObject(i)
                        val id = obj.optInt("id")
                        val titulo = obj.optString("titulo")
                        val texto = obj.optString("texto")
                        val origenNombre = obj.optString("origen")
                        val hora = obj.optString("hora")
                        val origen = try {
                            Origen.valueOf(origenNombre)
                        } catch (e: Exception) {
                            null
                        }
                        if (origen != null) {
                            guardados.add(Aviso(id, titulo, texto, origen, hora))
                        }
                    }
                    val maxId = guardados.maxOfOrNull { it.id } ?: 0
                    synchronized(this@AvisosRepositoryDataStore) {
                        if (maxId >= siguiente) {
                            siguiente = maxId + 1
                        }
                        val actuales = _avisos.value
                        _avisos.value = actuales + guardados
                    }
                }
            } catch (e: Exception) {
                // Si el JSON está corrupto o vacío, se ignora el error
            }
        }
    }

    override fun registrar(titulo: String, texto: String, origen: Origen): Aviso {
        val hora = SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date())
        val aviso: Aviso
        val listaActualizada: List<Aviso>
        synchronized(this) {
            aviso = Aviso(siguiente++, titulo, texto, origen, hora)
            listaActualizada = listOf(aviso) + _avisos.value
            _avisos.value = listaActualizada
        }
        guardarEnSegundoPlano(listaActualizada)
        return aviso
    }

    private fun guardarEnSegundoPlano(lista: List<Aviso>) {
        scope.launch {
            try {
                val aGuardar = lista.take(50)
                val array = JSONArray()
                for (av in aGuardar) {
                    val obj = JSONObject().apply {
                        put("id", av.id)
                        put("titulo", av.titulo)
                        put("texto", av.texto)
                        put("origen", av.origen.name)
                        put("hora", av.hora)
                    }
                    array.put(obj)
                }
                dataStore.edit { prefs ->
                    prefs[KEY_HISTORIAL] = array.toString()
                }
            } catch (e: Exception) {
                // Ignorar errores al guardar
            }
        }
    }

    companion object {
        private val KEY_HISTORIAL = stringPreferencesKey("historial")
    }
}
