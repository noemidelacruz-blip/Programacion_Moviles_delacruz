package com.delacruz.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delacruz.huellitasvet.data.local.entity.Mascota
import com.delacruz.huellitasvet.data.local.relation.CitaConDetalle
import com.delacruz.huellitasvet.data.repository.VetRepository
import com.delacruz.huellitasvet.model.Especie
import com.delacruz.huellitasvet.model.Sexo
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate

// Errores del formulario, uno por campo (null = el campo esta bien)
data class ErroresMascota(
    val nombre: String? = null,
    val especie: String? = null,
    val sexo: String? = null,
    val anio: String? = null,
    val peso: String? = null
) {
    val hayErrores: Boolean
        get() = nombre != null || especie != null || sexo != null || anio != null || peso != null
}

// ViewModel de las mascotas: validaciones del formulario, eliminar con regla de citas
// y los datos de la ficha (mascota, historial clinico y vacunas).
// Solo conoce el Repository (interface), nunca el DAO ni las pantallas
class MascotaViewModel(
    private val repo: VetRepository,
    private val duenoId: Int
) : ViewModel() {

    // Errores por campo del formulario
    private val _errores = MutableStateFlow(ErroresMascota())
    val errores: StateFlow<ErroresMascota> = _errores.asStateFlow()

    // Mensaje para el usuario (por ejemplo, no se puede eliminar)
    private val _mensaje = MutableStateFlow<String?>(null)
    val mensaje: StateFlow<String?> = _mensaje.asStateFlow()

    // Eventos de una sola vez: la mascota se guardo o se elimino
    private val _guardado = MutableStateFlow(false)
    val guardado: StateFlow<Boolean> = _guardado.asStateFlow()
    private val _eliminada = MutableStateFlow(false)
    val eliminada: StateFlow<Boolean> = _eliminada.asStateFlow()

    // Cada flujo se crea una sola vez por mascota y se reutiliza
    private val mascotas = mutableMapOf<Int, StateFlow<Mascota?>>()
    private val historiales = mutableMapOf<Int, StateFlow<List<CitaConDetalle>>>()
    private val vacunas = mutableMapOf<Int, StateFlow<Boolean>>()

    // Una mascota por id (ficha y formulario de edicion)
    fun mascota(id: Int): StateFlow<Mascota?> = mascotas.getOrPut(id) {
        repo.observarMascota(id)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)
    }

    // Historial clinico: citas ATENDIDAS de la mascota, la mas reciente primero
    fun historial(mascotaId: Int): StateFlow<List<CitaConDetalle>> = historiales.getOrPut(mascotaId) {
        repo.observarHistorial(mascotaId)
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())
    }

    // Vacunas al dia: tiene una vacunacion atendida en los ultimos 12 meses
    fun vacunasAlDia(mascotaId: Int): StateFlow<Boolean> = vacunas.getOrPut(mascotaId) {
        historial(mascotaId)
            .map { lista ->
                val limite = LocalDate.now().minusYears(1)
                lista.any { c ->
                    c.servicio.nombre.contains("vacun", ignoreCase = true) &&
                            !LocalDate.parse(c.cita.fecha).isBefore(limite)
                }
            }
            .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), false)
    }

    // Valida los campos y guarda la mascota (nueva si id = 0, o editada si ya existe)
    fun guardar(
        id: Int,
        nombre: String,
        especie: Especie?,
        raza: String,
        anio: String,
        peso: String,
        sexo: Sexo?
    ) {
        val anioActual = LocalDate.now().year
        val anioNumero = anio.trim().toIntOrNull()
        // Se acepta coma o punto como separador decimal
        val pesoNumero = peso.trim().replace(',', '.').toDoubleOrNull()

        val errores = ErroresMascota(
            nombre = if (nombre.trim().length < 2) "Escribe al menos 2 caracteres" else null,
            especie = if (especie == null) "Elige una especie" else null,
            sexo = if (sexo == null) "Elige el sexo" else null,
            anio = if (anioNumero == null || anioNumero !in 1990..anioActual)
                "Entre 1990 y $anioActual" else null,
            peso = if (pesoNumero == null || pesoNumero <= 0.0) "Debe ser mayor que 0" else null
        )
        _errores.value = errores
        if (errores.hayErrores || especie == null || sexo == null ||
            anioNumero == null || pesoNumero == null
        ) return

        viewModelScope.launch {
            // Si se edita, se parte de la mascota guardada para conservar su dueno y su id
            val actual = if (id != 0) repo.observarMascota(id).first() else null
            val mascota = (actual ?: Mascota(
                duenoId = duenoId,
                nombre = "",
                especie = "",
                raza = "",
                anioNacimiento = 0,
                peso = 0.0,
                sexo = ""
            )).copy(
                nombre = nombre.trim(),
                especie = especie.name,
                raza = raza.trim().ifBlank { "Mestizo" },
                anioNacimiento = anioNumero,
                peso = pesoNumero,
                sexo = sexo.name
            )
            repo.guardarMascota(mascota)
            _guardado.value = true
        }
    }

    // Elimina la mascota solo si no tiene citas; si tiene, avisa y no la borra
    fun eliminar(mascota: Mascota) {
        viewModelScope.launch {
            if (repo.mascotaTieneCitas(mascota.id)) {
                _mensaje.value = "No se puede eliminar a ${mascota.nombre}: tiene citas registradas"
            } else {
                repo.eliminarMascota(mascota)
                _eliminada.value = true
            }
        }
    }

    fun limpiarMensaje() {
        _mensaje.value = null
    }

    // La pantalla ya reacciono al evento (volver atras); se reinicia para la proxima vez
    fun consumirEventos() {
        _guardado.value = false
        _eliminada.value = false
    }
}