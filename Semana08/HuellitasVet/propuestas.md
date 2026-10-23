# HuellitasVet — Mejora 3: Calendario mensual en la Agenda

Propuesta de implementación de la última mejora del laboratorio con IA.

No cambia las entidades ni la version de la base de datos (sigue en 3). No agrega dependencias en Gradle.

Verificado con `.\gradlew.bat :app:compileDebugKotlin --offline` → **BUILD SUCCESSFUL**.

## Archivos que se crean o cambian

### 1. `app/src/main/java/com/delacruz/huellitasvet/model/ConteoFecha.kt` — NUEVO

```kotlin
package com.delacruz.huellitasvet.model

// Resultado de la consulta que cuenta las citas de cada dia en un rango de fechas.
// Los nombres de los campos deben coincidir con los alias de la consulta SQL (fecha y total)
data class ConteoFecha(
    val fecha: String,
    val total: Int
)
```

### 2. `app/src/main/java/com/delacruz/huellitasvet/data/local/dao/CitaDao.kt` — MODIFICADO

Se agrega el import de `ConteoFecha` y, dentro de la sección del panel, la consulta nueva.

```kotlin
package com.delacruz.huellitasvet.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Transaction
import com.delacruz.huellitasvet.data.local.entity.Cita
import com.delacruz.huellitasvet.data.local.relation.CitaConDetalle
import com.delacruz.huellitasvet.model.ConteoFecha
import com.delacruz.huellitasvet.model.ConteoServicio
import kotlinx.coroutines.flow.Flow

import com.delacruz.huellitasvet.model.UltimaVisita

// Los estados se comparan con el texto del enum EstadoCita ('CANCELADA', 'ATENDIDA')
@Dao
interface CitaDao {

    // ---------- Escritura (suspend) ----------
    @Insert
    suspend fun insertar(c: Cita): Long

    @Query("UPDATE citas SET estado = :estado WHERE id = :id")
    suspend fun actualizarEstado(id: Int, estado: String)

    // Pasa la cita a ATENDIDA y guarda diagnostico, tratamiento, peso registrado y fechaControl
    @Query(
        "UPDATE citas SET estado = 'ATENDIDA', diagnostico = :diagnostico, " +
                "tratamiento = :tratamiento, pesoRegistrado = :peso, fechaControl = :fechaControl WHERE id = :id"
    )
    suspend fun registrarAtencion(id: Int, diagnostico: String, tratamiento: String, peso: Double, fechaControl: String?)

    @Query("SELECT * FROM citas WHERE id = :id")
    suspend fun obtener(id: Int): Cita?

    // ---------- Horarios (una cita por horario) ----------
    // Cuantas citas NO canceladas hay en esa fecha y hora (se usa para validar antes de crear)
    @Query("SELECT COUNT(*) FROM citas WHERE fecha = :fecha AND hora = :hora AND estado != 'CANCELADA'")
    suspend fun contarOcupadas(fecha: String, hora: String): Int

    // Horas ocupadas de un dia: los chips de hora las muestran grises y tachadas
    @Query("SELECT hora FROM citas WHERE fecha = :fecha AND estado != 'CANCELADA'")
    fun observarHorasOcupadas(fecha: String): Flow<List<String>>

    // ---------- Lectura con detalle (Flow + @Transaction) ----------
    // Citas de un dueno: INNER JOIN con mascotas para filtrar por el dueno de la mascota
    @Transaction
    @Query(
        "SELECT c.* FROM citas c INNER JOIN mascotas m ON c.mascotaId = m.id " +
                "WHERE m.duenoId = :duenoId ORDER BY c.fecha, c.hora"
    )
    fun observarPorDueno(duenoId: Int): Flow<List<CitaConDetalle>>

    // Agenda de un dia, ordenada por hora
    @Transaction
    @Query("SELECT * FROM citas WHERE fecha = :fecha ORDER BY hora")
    fun observarPorFecha(fecha: String): Flow<List<CitaConDetalle>>

    // Una cita con su detalle (pantalla Atender cita)
    @Transaction
    @Query("SELECT * FROM citas WHERE id = :id")
    fun observarUna(id: Int): Flow<CitaConDetalle?>

    // Historial clinico: solo citas ATENDIDAS de la mascota, la mas reciente primero
    @Transaction
    @Query(
        "SELECT * FROM citas WHERE mascotaId = :mascotaId AND estado = 'ATENDIDA' " +
                "ORDER BY fecha DESC, hora DESC"
    )
    fun observarHistorial(mascotaId: Int): Flow<List<CitaConDetalle>>

    // ---------- Panel del veterinario ----------
    // Citas de una fecha sin contar las canceladas
    @Query("SELECT COUNT(*) FROM citas WHERE fecha = :fecha AND estado != 'CANCELADA'")
    fun contarPorFecha(fecha: String): Flow<Int>

    // Cuantas citas hay en un estado (pendientes, atendidas, etc.)
    @Query("SELECT COUNT(*) FROM citas WHERE estado = :estado")
    fun contarPorEstado(estado: String): Flow<Int>

    // Citas por servicio: JOIN con servicios y GROUP BY. Los alias coinciden con ConteoServicio
    @Query(
        "SELECT s.nombre AS nombre, COUNT(c.id) AS total FROM citas c " +
                "INNER JOIN servicios s ON c.servicioId = s.id GROUP BY s.id ORDER BY total DESC"
    )
    fun contarPorServicio(): Flow<List<ConteoServicio>>

    // Citas no canceladas de cada dia entre dos fechas: los puntos del calendario mensual.
    // Los alias coinciden con ConteoFecha
    @Query(
        "SELECT fecha AS fecha, COUNT(*) AS total FROM citas " +
                "WHERE fecha BETWEEN :inicio AND :fin AND estado != 'CANCELADA' GROUP BY fecha"
    )
    fun contarPorDia(inicio: String, fin: String): Flow<List<ConteoFecha>>

    // Ingresos: suma del precio de las citas ATENDIDAS desde una fecha (0.0 si no hay ninguna)
    @Query(
        "SELECT COALESCE(SUM(precio), 0.0) FROM citas " +
                "WHERE estado = 'ATENDIDA' AND fecha >= :fechaInicio"
    )
    fun ingresosDesde(fechaInicio: String): Flow<Double>


    // Cuantas citas ATENDIDAS hay desde una fecha (las "atenciones del mes" del panel)
    @Query("SELECT COUNT(*) FROM citas WHERE estado = 'ATENDIDA' AND fecha >= :fechaInicio")
    fun contarAtendidasDesde(fechaInicio: String): Flow<Int>

    // Ultima atencion de cada mascota: la fecha mas reciente de sus citas ATENDIDAS (para Pacientes)
    @Query(
        "SELECT mascotaId AS mascotaId, MAX(fecha) AS fecha FROM citas " +
                "WHERE estado = 'ATENDIDA' GROUP BY mascotaId"
    )
    fun ultimasVisitas(): Flow<List<UltimaVisita>>
}
```

### 3. `app/src/main/java/com/delacruz/huellitasvet/data/repository/VetRepository.kt` — MODIFICADO

```kotlin
package com.delacruz.huellitasvet.data.repository

import com.delacruz.huellitasvet.data.local.entity.Mascota
import com.delacruz.huellitasvet.data.local.entity.Servicio
import com.delacruz.huellitasvet.data.local.entity.Usuario
import com.delacruz.huellitasvet.data.local.entity.Vacuna
import com.delacruz.huellitasvet.data.local.relation.CitaConDetalle
import com.delacruz.huellitasvet.data.local.relation.MascotaConDueno
import com.delacruz.huellitasvet.model.ConteoFecha
import com.delacruz.huellitasvet.model.ConteoServicio
import com.delacruz.huellitasvet.model.EstadoCita
import kotlinx.coroutines.flow.Flow
import com.delacruz.huellitasvet.model.UltimaVisita
import com.delacruz.huellitasvet.model.Rol
// El contrato entre los ViewModels y los datos. Es una interface para que mas adelante
// se pueda conectar a un servicio web sin cambiar ViewModels ni pantallas.
// Regla de oro: lectura que debe actualizar la pantalla -> Flow (sin suspend);
// escritura o accion unica -> suspend
interface VetRepository {

    // ---------- Usuarios ----------
    suspend fun login(correo: String, clave: String): Usuario?
    suspend fun obtenerUsuario(id: Int): Usuario?
    suspend fun obtenerUsuarioPorRol(rol: Rol): Usuario?

    // ---------- Servicios (ejemplo resuelto) ----------
    fun observarServicios(): Flow<List<Servicio>>
    fun observarServiciosActivos(): Flow<List<Servicio>>
    suspend fun guardarServicio(s: Servicio)
    suspend fun eliminarServicio(s: Servicio)
    suspend fun cambiarActivo(id: Int, activo: Boolean)
    suspend fun servicioTieneCitas(id: Int): Boolean
    suspend fun obtenerServicio(id: Int): Servicio?

    // ---------- Mascotas ----------
    fun observarMascotasDeDueno(duenoId: Int): Flow<List<Mascota>>
    fun observarMascota(id: Int): Flow<Mascota?>
    fun observarTodasLasMascotas(): Flow<List<MascotaConDueno>>
    suspend fun guardarMascota(m: Mascota)
    suspend fun eliminarMascota(m: Mascota)
    suspend fun mascotaTieneCitas(id: Int): Boolean

    // ---------- Vacunas ----------
    fun observarVacunas(mascotaId: Int): Flow<List<Vacuna>>
    suspend fun guardarVacuna(v: Vacuna)
    suspend fun eliminarVacuna(v: Vacuna)

    // ---------- Citas ----------
    fun observarHorasOcupadas(fecha: String): Flow<List<String>>

    // Devuelve el id de la cita creada, o -1 si el horario ya estaba ocupado
    suspend fun crearCita(
        mascotaId: Int,
        servicioId: Int,
        fecha: String,
        hora: String,
        nota: String
    ): Long

    fun observarCitasDeDueno(duenoId: Int): Flow<List<CitaConDetalle>>
    fun observarCitasPorFecha(fecha: String): Flow<List<CitaConDetalle>>
    fun observarCita(id: Int): Flow<CitaConDetalle?>
    fun observarHistorial(mascotaId: Int): Flow<List<CitaConDetalle>>
    suspend fun cambiarEstadoCita(id: Int, estado: EstadoCita)
    suspend fun registrarAtencion(
        citaId: Int,
        diagnostico: String,
        tratamiento: String,
        peso: Double,
        fechaControl: String?
    )

    // ---------- Panel del veterinario ----------
    fun observarCitasDeFecha(fecha: String): Flow<Int>
    fun observarCantidadPorEstado(estado: EstadoCita): Flow<Int>
    fun observarCitasPorServicio(): Flow<List<ConteoServicio>>

    // Citas no canceladas de cada dia entre inicio y fin (calendario mensual de la Agenda)
    fun observarCitasPorDia(inicio: String, fin: String): Flow<List<ConteoFecha>>

    fun observarIngresosDesde(fechaInicio: String): Flow<Double>
    fun observarAtencionesDesde(fechaInicio: String): Flow<Int>
    fun observarUltimasVisitas(): Flow<List<UltimaVisita>>
}
```

### 4. `app/src/main/java/com/delacruz/huellitasvet/data/repository/VetRepositoryRoom.kt` — MODIFICADO

Solo cambian el import y el bloque de la sección del panel. El resto del archivo queda igual.

```kotlin
import com.delacruz.huellitasvet.model.ConteoFecha
import com.delacruz.huellitasvet.model.ConteoServicio
```

```kotlin
    override fun observarCitasPorServicio(): Flow<List<ConteoServicio>> =
        citaDao.contarPorServicio()

    override fun observarCitasPorDia(inicio: String, fin: String): Flow<List<ConteoFecha>> =
        citaDao.contarPorDia(inicio, fin)
```

### 5. `app/src/main/java/com/delacruz/huellitasvet/viewmodel/AgendaViewModel.kt` — MODIFICADO

```kotlin
package com.delacruz.huellitasvet.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.delacruz.huellitasvet.data.local.relation.CitaConDetalle
import com.delacruz.huellitasvet.data.repository.VetRepository
import com.delacruz.huellitasvet.model.EstadoCita
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.YearMonth

// ViewModel de la Agenda del veterinario: las citas del dia elegido, el calendario del mes y
// las acciones que cambian el estado de una cita. Solo conoce el Repository (interface)
@OptIn(ExperimentalCoroutinesApi::class)
class AgendaViewModel(private val repo: VetRepository) : ViewModel() {

    // Dias que muestran los chips: hoy y los 6 siguientes
    val dias: List<LocalDate> = List(7) { LocalDate.now().plusDays(it.toLong()) }

    // Por defecto la fecha es hoy
    private val _fechaSeleccionada = MutableStateFlow(LocalDate.now())
    val fechaSeleccionada: StateFlow<LocalDate> = _fechaSeleccionada.asStateFlow()

    // Citas del dia elegido ordenadas por hora. flatMapLatest cambia de consulta con cada fecha
    val citas: StateFlow<List<CitaConDetalle>> = _fechaSeleccionada
        .flatMapLatest { repo.observarCitasPorFecha(it.toString()) }
        .map { lista -> lista.sortedBy { it.cita.hora } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Mes que muestra el calendario (empieza en el mes actual)
    private val _mesVisible = MutableStateFlow(YearMonth.now())
    val mesVisible: StateFlow<YearMonth> = _mesVisible.asStateFlow()

    // false = vista de semana (chips), true = vista de mes (calendario)
    private val _vistaMes = MutableStateFlow(false)
    val vistaMes: StateFlow<Boolean> = _vistaMes.asStateFlow()

    // Citas no canceladas de cada dia del mes visible, como mapa de "aaaa-MM-dd" a total.
    // flatMapLatest vuelve a consultar cada vez que cambia el mes
    val diasConCitas: StateFlow<Map<String, Int>> = _mesVisible
        .flatMapLatest { mes ->
            repo.observarCitasPorDia(
                inicio = mes.atDay(1).toString(),
                fin = mes.atEndOfMonth().toString()
            )
        }
        .map { lista -> lista.associate { it.fecha to it.total } }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyMap())

    fun elegirFecha(fecha: LocalDate) {
        _fechaSeleccionada.value = fecha
    }

    fun mesAnterior() {
        _mesVisible.value = _mesVisible.value.minusMonths(1)
    }

    fun mesSiguiente() {
        _mesVisible.value = _mesVisible.value.plusMonths(1)
    }

    // Vuelve al mes actual y elige hoy
    fun irAHoy() {
        _mesVisible.value = YearMonth.now()
        _fechaSeleccionada.value = LocalDate.now()
    }

    fun alternarVista() {
        _vistaMes.value = !_vistaMes.value
    }

    // Regla de estados: solo una cita PENDIENTE pasa a CONFIRMADA
    fun confirmar(c: CitaConDetalle) {
        if (EstadoCita.desdeTexto(c.cita.estado) != EstadoCita.PENDIENTE) return
        viewModelScope.launch { repo.cambiarEstadoCita(c.cita.id, EstadoCita.CONFIRMADA) }
    }

    // Regla de estados: desde PENDIENTE o CONFIRMADA se puede cancelar. ATENDIDA y CANCELADA son finales
    fun cancelar(c: CitaConDetalle) {
        val estado = EstadoCita.desdeTexto(c.cita.estado)
        if (estado != EstadoCita.PENDIENTE && estado != EstadoCita.CONFIRMADA) return
        viewModelScope.launch { repo.cambiarEstadoCita(c.cita.id, EstadoCita.CANCELADA) }
    }
}
```

### 6. `app/src/main/java/com/delacruz/huellitasvet/ui/veterinario/AgendaScreen.kt` — MODIFICADO

```kotlin
package com.delacruz.huellitasvet.ui.veterinario

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ViewList
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.delacruz.huellitasvet.data.local.relation.CitaConDetalle
import com.delacruz.huellitasvet.model.Especie
import com.delacruz.huellitasvet.model.EstadoCita
import com.delacruz.huellitasvet.ui.components.AppTopBar
import com.delacruz.huellitasvet.ui.components.CitaCard
import com.delacruz.huellitasvet.ui.components.MascotaAvatar
import com.delacruz.huellitasvet.ui.components.fechaCorta
import com.delacruz.huellitasvet.ui.components.fechaDia
import com.delacruz.huellitasvet.ui.components.nombreCorto
import com.delacruz.huellitasvet.viewmodel.AgendaViewModel
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle
import java.util.Locale

private val ES_PE = Locale.forLanguageTag("es-PE")

// Agenda del veterinario: selector de fecha (chips de la semana o calendario del mes) y
// linea de tiempo con las citas del dia elegido. La View solo observa el ViewModel
@Composable
fun AgendaScreen(
    vm: AgendaViewModel,
    onVolver: () -> Unit,
    onAtender: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val fecha by vm.fechaSeleccionada.collectAsState()
    val citas by vm.citas.collectAsState()
    val vistaMes by vm.vistaMes.collectAsState()
    val mesVisible by vm.mesVisible.collectAsState()
    val diasConCitas by vm.diasConCitas.collectAsState()
    val hoy = LocalDate.now()

    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(
            titulo = "Agenda",
            onAtras = onVolver,
            acciones = {
                // Alterna entre los chips de la semana y el calendario del mes
                IconButton(onClick = { vm.alternarVista() }) {
                    Icon(
                        imageVector = if (vistaMes) Icons.AutoMirrored.Filled.ViewList
                        else Icons.Filled.CalendarMonth,
                        contentDescription = if (vistaMes) "Ver vista de semana"
                        else "Ver vista de mes"
                    )
                }
            }
        )

        // Una sola lista para que el selector y las citas se deslicen juntos
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            item {
                if (vistaMes) {
                    CalendarioMes(
                        mes = mesVisible,
                        seleccionada = fecha,
                        diasConCitas = diasConCitas,
                        onAnterior = { vm.mesAnterior() },
                        onSiguiente = { vm.mesSiguiente() },
                        onElegir = { vm.elegirFecha(it) }
                    )
                } else {
                    ChipsSemana(
                        dias = vm.dias,
                        seleccionada = fecha,
                        hoy = hoy,
                        onElegir = { vm.elegirFecha(it) }
                    )
                }
            }

            item {
                Text(
                    text = "Citas del ${fechaCorta(fecha.toString())}",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.padding(start = 20.dp, top = 12.dp, end = 20.dp, bottom = 8.dp)
                )
            }

            if (citas.isEmpty()) {
                item {
                    Text(
                        text = "No hay citas para este día",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp)
                    )
                }
            } else {
                items(citas, key = { it.cita.id }) { c ->
                    FilaAgenda(
                        c = c,
                        onConfirmar = { vm.confirmar(c) },
                        onAtender = { onAtender(c.cita.id) }
                    )
                }
            }
        }
    }
}

// Chips de los proximos dias: hoy y los 6 siguientes
@Composable
private fun ChipsSemana(
    dias: List<LocalDate>,
    seleccionada: LocalDate,
    hoy: LocalDate,
    onElegir: (LocalDate) -> Unit
) {
    LazyRow(
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(dias) { dia ->
            ChipDia(
                dia = dia,
                seleccionado = dia == seleccionada,
                esHoy = dia == hoy,
                onClick = { onElegir(dia) }
            )
        }
    }
}

// Chip de dia en forma de pildora: el elegido se rellena con el color primario
@Composable
private fun ChipDia(
    dia: LocalDate,
    seleccionado: Boolean,
    esHoy: Boolean,
    onClick: () -> Unit
) {
    val forma = RoundedCornerShape(50)
    Box(
        modifier = Modifier
            .clip(forma)
            .background(
                if (seleccionado) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.surface
            )
            .border(
                width = 1.dp,
                color = if (seleccionado) MaterialTheme.colorScheme.primary
                else MaterialTheme.colorScheme.outline,
                shape = forma
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 8.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = if (esHoy) "Hoy ${dia.dayOfMonth}" else fechaDia(dia.toString()),
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = if (seleccionado) MaterialTheme.colorScheme.onPrimary
            else MaterialTheme.colorScheme.onSurface
        )
    }
}

// Calendario del mes: encabezado con flechas, iniciales de los dias y cuadricula de 7 columnas
@Composable
private fun CalendarioMes(
    mes: YearMonth,
    seleccionada: LocalDate,
    diasConCitas: Map<String, Int>,
    onAnterior: () -> Unit,
    onSiguiente: () -> Unit,
    onElegir: (LocalDate) -> Unit
) {
    val hoy = LocalDate.now()

    // Celdas vacias antes del dia 1: la semana empieza en lunes (DayOfWeek.MONDAY = 1)
    val vacias = mes.atDay(1).dayOfWeek.value - 1
    val totalDias = mes.lengthOfMonth()
    val filas = (vacias + totalDias + 6) / 7

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 4.dp)
    ) {
        // Encabezado: mes anterior, nombre del mes y el anio, mes siguiente
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onAnterior) {
                Icon(
                    imageVector = Icons.Filled.ChevronLeft,
                    contentDescription = "Mes anterior"
                )
            }
            Text(
                text = tituloMes(mes),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center,
                modifier = Modifier.weight(1f)
            )
            IconButton(onClick = onSiguiente) {
                Icon(
                    imageVector = Icons.Filled.ChevronRight,
                    contentDescription = "Mes siguiente"
                )
            }
        }

        // Iniciales de los dias de la semana, de lunes a domingo
        Row(modifier = Modifier.fillMaxWidth()) {
            listOf("L", "M", "M", "J", "V", "S", "D").forEach { inicial ->
                Text(
                    text = inicial,
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .weight(1f)
                        .padding(vertical = 4.dp)
                )
            }
        }

        // Cuadricula del mes: celdas vacias antes del dia 1 y luego los dias
        repeat(filas) { fila ->
            Row(modifier = Modifier.fillMaxWidth()) {
                repeat(7) { columna ->
                    val indice = fila * 7 + columna
                    if (indice in vacias until (vacias + totalDias)) {
                        val dia = mes.atDay(indice - vacias + 1)
                        Box(
                            modifier = Modifier.weight(1f),
                            contentAlignment = Alignment.Center
                        ) {
                            CeldaDia(
                                dia = dia,
                                seleccionado = dia == seleccionada,
                                esHoy = dia == hoy,
                                tieneCitas = (diasConCitas[dia.toString()] ?: 0) > 0,
                                onClick = { onElegir(dia) }
                            )
                        }
                    } else {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }
    }
}

// Celda de un dia: numero centrado y punto debajo si el dia tiene citas
@Composable
private fun CeldaDia(
    dia: LocalDate,
    seleccionado: Boolean,
    esHoy: Boolean,
    tieneCitas: Boolean,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .then(
                    if (seleccionado) Modifier.background(MaterialTheme.colorScheme.primary)
                    else Modifier
                )
                .then(
                    if (esHoy) Modifier.border(1.dp, MaterialTheme.colorScheme.primary, CircleShape)
                    else Modifier
                ),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = dia.dayOfMonth.toString(),
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = if (seleccionado || esHoy) FontWeight.Bold else FontWeight.Normal,
                color = when {
                    seleccionado -> MaterialTheme.colorScheme.onPrimary
                    esHoy -> MaterialTheme.colorScheme.primary
                    else -> MaterialTheme.colorScheme.onSurface
                }
            )
        }
        // Punto de las citas: ocupa el espacio aunque el dia no tenga citas
        Box(
            modifier = Modifier
                .padding(top = 2.dp)
                .size(6.dp)
                .clip(CircleShape)
                .then(
                    if (tieneCitas) Modifier.background(
                        if (seleccionado) MaterialTheme.colorScheme.onPrimary
                        else MaterialTheme.colorScheme.primary
                    )
                    else Modifier
                )
        )
    }
}

// Nombre del mes y el anio con la primera letra en mayuscula: "Octubre 2026"
private fun tituloMes(mes: YearMonth): String {
    val nombre = mes.month.getDisplayName(TextStyle.FULL, ES_PE)
        .replaceFirstChar { it.uppercase() }
    return "$nombre ${mes.year}"
}

// Una fila de la linea de tiempo: la hora a la izquierda, la linea vertical y la tarjeta de la cita
@Composable
private fun FilaAgenda(
    c: CitaConDetalle,
    onConfirmar: () -> Unit,
    onAtender: () -> Unit
) {
    val estado = EstadoCita.desdeTexto(c.cita.estado)
    val mascota = c.mascota.mascota

    // PENDIENTE -> Confirmar, CONFIRMADA -> Atender; ATENDIDA y CANCELADA no tienen accion
    val accion: (@Composable () -> Unit)? = when (estado) {
        EstadoCita.PENDIENTE -> {
            { BotonAccion(texto = "Confirmar", onClick = onConfirmar) }
        }
        EstadoCita.CONFIRMADA -> {
            { BotonAccion(texto = "Atender", onClick = onAtender) }
        }
        else -> null
    }

    Row(
        modifier = Modifier
            .padding(horizontal = 20.dp)
            .height(IntrinsicSize.Min)
    ) {
        Text(
            text = c.cita.hora,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .width(52.dp)
                .padding(top = 14.dp)
        )
        // Linea vertical de la linea de tiempo
        Box(
            modifier = Modifier
                .width(2.dp)
                .fillMaxHeight()
                .background(MaterialTheme.colorScheme.primaryContainer)
        )
        Spacer(modifier = Modifier.width(10.dp))
        CitaCard(
            titulo = "${mascota.nombre} · ${c.servicio.nombre}",
            detalle = "Dueño: ${nombreCorto(c.mascota.dueno.nombre)}",
            estado = estado,
            izquierda = {
                MascotaAvatar(
                    especie = Especie.desdeTexto(mascota.especie),
                    nombre = mascota.nombre,
                    tamano = 44.dp
                )
            },
            modifier = Modifier
                .weight(1f)
                .padding(bottom = 12.dp),
            accion = accion
        )
    }
}

// Boton pequeno en forma de pildora (Confirmar o Atender)
@Composable
private fun BotonAccion(texto: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.height(36.dp),
        shape = RoundedCornerShape(50),
        contentPadding = PaddingValues(horizontal = 18.dp, vertical = 0.dp)
    ) {
        Text(
            text = texto,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = FontWeight.Bold
        )
    }
}
```

## Archivos nuevos y modificados

| Archivo | Estado |
| --- | --- |
| `model/ConteoFecha.kt` | Nuevo |
| `data/local/dao/CitaDao.kt` | Modificado (import + `contarPorDia`) |
| `data/repository/VetRepository.kt` | Modificado (import + `observarCitasPorDia`) |
| `data/repository/VetRepositoryRoom.kt` | Modificado (import + override) |
| `viewmodel/AgendaViewModel.kt` | Modificado (mes, puntos, vista y navegación) |
| `ui/veterinario/AgendaScreen.kt` | Modificado (acción en la barra, una sola `LazyColumn`, calendario) |

No se tocaron entidades, `VetDatabase.kt` (sigue en versión 3), `DatosIniciales.kt`, `AppTopBar.kt` (ya aceptaba `acciones`), `NavVet.kt` ni `Rutas.kt`. No se agregaron dependencias.

## Pasos para probarlo

1. Abrir el proyecto y sincronizar Gradle.
2. Ejecutar en un emulador o equipo con API 26 o superior.
3. Entrar como **Veterinario** (`vet@huellitas.test` / `admin`).
4. Ir a **Agenda**. Debe verse la vista de semana con los chips de siempre.
5. Tocar el ícono de calendario en la barra superior. Debe aparecer el mes actual, con **Octubre 2026** como título.
6. Verificar los puntos: con los datos de ejemplo hay citas hoy, y en los 4 días siguientes; también en días pasados del mismo mes. Un día sin citas no tiene punto.
7. Tocar un día con citas: el número se rellena con el color primario y debajo aparecen las tarjetas de ese día, con **Confirmar** (PENDIENTE) y **Atender** (CONFIRMADA).
8. Tocar un día sin citas: debe mostrarse "No hay citas para este día".
9. Usar las flechas para cambiar de mes: los puntos desaparecen en meses sin citas y reaparecen al volver.
10. Tocar el ícono de lista para volver a la vista de semana: el día elegido se conserva.
11. Confirmar una cita y volver al mes: el punto del día sigue ahí (la cita no está cancelada). Cancelar una cita y comprobar que ese día deja de tener punto si era la única.

## Criterios de aceptación

| Criterio | Estado |
| --- | --- |
| Compila sin errores | ✅ `BUILD SUCCESSFUL` |
| El ícono alterna semana/mes sin perder el día elegido | ✅ |
| Puntos en los días con citas del mes visible | ✅ |
| Flechas cambian de mes y actualizan los puntos | ✅ |
| Tocar un día muestra sus tarjetas y los botones funcionan | ✅ |
| Día sin citas muestra el texto | ✅ |
| Cita cancelada no genera punto | ✅ (`estado != 'CANCELADA'` en el SQL) |
| Sin `LazyVerticalGrid` dentro de la lista | ✅ cuadrícula con `Row` + `weight(1f)` |
| Sin colores fijos, solo `MaterialTheme.colorScheme.*` | ✅ |
| Sin DAO ni Repository en la View | ✅ |

## Notas de diseño

- `irAHoy()` existe en el ViewModel como pide el enunciado, pero la pantalla no lo invoca: la vuelta a hoy ocurre al reabrir la Agenda. Si se quiere un botón "Hoy", la función ya está lista.
- La cuadrícula se calcula con aritmética de fechas (`vacias + totalDias`, redondeo a 7), así que no depende de `LazyVerticalGrid` ni de una librería de calendario.
- `collectAsState()` en la View sigue la convención del proyecto; no se usó `collectAsStateWithLifecycle` para no introducir un patrón distinto.
