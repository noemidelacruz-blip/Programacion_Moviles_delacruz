package com.delacruz.saludpluscitas.ui.screens.doctores

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.delacruz.saludpluscitas.data.repository.Repositorio
import com.delacruz.saludpluscitas.navigation.Rutas
import com.delacruz.saludpluscitas.ui.components.BarraSuperior
import com.delacruz.saludpluscitas.ui.components.TarjetaMedico
import com.delacruz.saludpluscitas.ui.theme.AzulPrimario
import com.delacruz.saludpluscitas.ui.theme.TextoSecundario

// Mis Doctores: lista de los médicos de la clínica organizados por especialidad.
// Al tocar un médico se abre la selección de fecha y hora para agendar su cita.
@Composable
fun MisDoctoresScreen(
    navController: NavController
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            BarraSuperior(
                titulo = "Mis Doctores",
                onAtras = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Repositorio.especialidades.forEach { especialidad ->
                val doctores = Repositorio.medicosPorEspecialidad(especialidad.id)

                if (doctores.isNotEmpty()) {
                    // Encabezado de la especialidad con la cantidad de doctores.
                    item(key = "especialidad_${especialidad.id}") {
                        Column {
                            Text(
                                text = especialidad.nombre,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = AzulPrimario
                            )
                            Text(
                                text = if (doctores.size == 1) "1 doctor" else "${doctores.size} doctores",
                                fontSize = 13.sp,
                                color = TextoSecundario
                            )
                        }
                    }

                    // Médicos de esa especialidad, mejor calificados primero.
                    items(doctores, key = { it.id }) { medico ->
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