package com.delacruz.saludpluscitas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.delacruz.saludpluscitas.data.repository.Repositorio
import com.delacruz.saludpluscitas.navigation.Rutas
import com.delacruz.saludpluscitas.ui.components.BarraSuperior
import com.delacruz.saludpluscitas.ui.components.CampoBusqueda
import com.delacruz.saludpluscitas.ui.components.EstadoVacio
import com.delacruz.saludpluscitas.ui.components.TarjetaMedico

@Composable
fun MedicosScreen(
    navController: NavController,
    especialidadId: Int
) {
    val especialidad = Repositorio.obtenerEspecialidad(especialidadId)

    // La lupa muestra u oculta el buscador.
    var buscando by remember { mutableStateOf(false) }
    var busqueda by remember { mutableStateOf("") }

    // buscarMedicos ya viene ordenado por calificación (usa medicosPorEspecialidad).
    val medicos = Repositorio.buscarMedicos(especialidadId, busqueda)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            BarraSuperior(
                titulo = "Médicos de ${especialidad?.nombre ?: ""}",
                onAtras = { navController.popBackStack() },
                acciones = {
                    IconButton(onClick = {
                        buscando = !buscando
                        if (!buscando) busqueda = ""
                    }) {
                        Icon(
                            imageVector = if (buscando) Icons.Default.Close else Icons.Default.Search,
                            contentDescription = "Buscar médico"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            if (buscando) {
                CampoBusqueda(
                    valor = busqueda,
                    onValorChange = { busqueda = it },
                    placeholder = "Buscar médico..."
                )
            }

            if (medicos.isEmpty()) {
                EstadoVacio(mensaje = "No se encontraron médicos")
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(medicos, key = { it.id }) { medico ->
                        TarjetaMedico(
                            medico = medico,
                            onClick = {
                                navController.navigate(Rutas.FechaHora.crearRuta(medico.id))
                            }
                        )
                    }
                }
            }
        }
    }
}