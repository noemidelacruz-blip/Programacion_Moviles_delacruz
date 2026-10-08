package com.delacruz.saludpluscitas.ui.screens.citas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EventBusy
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.delacruz.saludpluscitas.data.repository.Repositorio
import com.delacruz.saludpluscitas.navigation.Rutas
import com.delacruz.saludpluscitas.ui.components.BarraNavegacion
import com.delacruz.saludpluscitas.ui.components.BarraSuperior
import com.delacruz.saludpluscitas.ui.components.BotonPrimario
import com.delacruz.saludpluscitas.ui.components.EstadoVacio
import com.delacruz.saludpluscitas.ui.components.TarjetaCita

@Composable
fun MisCitasScreen(
    navController: NavController
) {
    // citas es un mutableStateListOf: si se agenda o cancela, la lista se recalcula sola.
    val citas = Repositorio.citasDelUsuario()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            BarraSuperior(
                titulo = "Mis citas",
                onAtras = { navController.popBackStack() }
            )
        },
        bottomBar = {
            BarraNavegacion(
                navController = navController,
                rutaActual = Rutas.MisCitas.ruta
            )
        }
    ) { innerPadding ->
        if (citas.isEmpty()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 20.dp)
            ) {
                EstadoVacio(
                    mensaje = "Aún no tienes citas agendadas",
                    icono = Icons.Default.EventBusy
                )
                Spacer(modifier = Modifier.height(8.dp))
                BotonPrimario(
                    texto = "Agendar una cita",
                    onClick = { navController.navigate(Rutas.Especialidades.ruta) }
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(citas, key = { it.id }) { cita ->
                    TarjetaCita(
                        cita = cita,
                        onClick = { navController.navigate(Rutas.DetalleCita.crearRuta(cita.id)) }
                    )
                }
            }
        }
    }
}