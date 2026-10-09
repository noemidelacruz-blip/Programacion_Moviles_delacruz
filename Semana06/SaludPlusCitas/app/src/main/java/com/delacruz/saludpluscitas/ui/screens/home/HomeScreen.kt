package com.delacruz.saludpluscitas.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EventAvailable
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Notifications
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.delacruz.saludpluscitas.data.repository.Repositorio
import com.delacruz.saludpluscitas.navigation.Rutas
import com.delacruz.saludpluscitas.ui.components.BarraNavegacion
import com.delacruz.saludpluscitas.ui.components.TarjetaAccion
import com.delacruz.saludpluscitas.ui.components.TarjetaEspecialidadDestacada
import com.delacruz.saludpluscitas.ui.theme.AzulPastel
import com.delacruz.saludpluscitas.ui.theme.AzulPrimario
import com.delacruz.saludpluscitas.ui.theme.LilaPastel
import com.delacruz.saludpluscitas.ui.theme.LilaTexto
import com.delacruz.saludpluscitas.ui.theme.NaranjaPastel
import com.delacruz.saludpluscitas.ui.theme.NaranjaTexto
import com.delacruz.saludpluscitas.ui.theme.TextoSecundario
import com.delacruz.saludpluscitas.ui.theme.VerdePastel
import com.delacruz.saludpluscitas.ui.theme.VerdeTexto

@Composable
fun HomeScreen(
    navController: NavController
) {
    // Solo el primer nombre para el saludo ("¡Hola, Noemí!").
    val nombre = Repositorio.usuarioActual?.nombre?.substringBefore(" ") ?: ""
    val destacadas = Repositorio.especialidadesDestacadas()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        bottomBar = {
            BarraNavegacion(
                navController = navController,
                rutaActual = Rutas.Home.ruta
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "¡Hola, $nombre!",
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "¿Qué deseas hacer hoy?",
                        fontSize = 15.sp,
                        color = TextoSecundario
                    )
                }
                IconButton(onClick = { navController.navigate(Rutas.Notificaciones.ruta) }) {
                    Icon(
                        imageVector = Icons.Outlined.Notifications,
                        contentDescription = "Notificaciones",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Grid 2x2 de accesos rápidos.
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TarjetaAccion(
                    titulo = "Agendar cita",
                    icono = Icons.Default.CalendarMonth,
                    colorFondo = AzulPastel,
                    colorContenido = AzulPrimario,
                    onClick = { navController.navigate(Rutas.Especialidades.ruta) },
                    modifier = Modifier.weight(1f)
                )
                TarjetaAccion(
                    titulo = "Mis citas",
                    icono = Icons.Default.EventAvailable,
                    colorFondo = VerdePastel,
                    colorContenido = VerdeTexto,
                    onClick = { navController.navigate(Rutas.MisCitas.ruta) },
                    modifier = Modifier.weight(1f)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                TarjetaAccion(
                    titulo = "Mis datos",
                    icono = Icons.Default.Person,
                    colorFondo = LilaPastel,
                    colorContenido = LilaTexto,
                    onClick = { navController.navigate(Rutas.Perfil.ruta) },
                    modifier = Modifier.weight(1f)
                )
                TarjetaAccion(
                    titulo = "Resultados",
                    icono = Icons.Default.Description,
                    colorFondo = NaranjaPastel,
                    colorContenido = NaranjaTexto,
                    onClick = { navController.navigate(Rutas.Resultados.ruta) },
                    modifier = Modifier.weight(1f)
                )
            }

            // Acceso a la lista de doctores organizados por especialidad.
            Spacer(modifier = Modifier.height(12.dp))
            TarjetaAccion(
                titulo = "Mis doctores",
                icono = Icons.Default.MedicalServices,
                colorFondo = AzulPastel,
                colorContenido = AzulPrimario,
                onClick = { navController.navigate(Rutas.MisDoctores.ruta) },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Especialidades destacadas",
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    modifier = Modifier.weight(1f)
                )
                TextButton(onClick = { navController.navigate(Rutas.Especialidades.ruta) }) {
                    Text(text = "Ver todas", color = AzulPrimario)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                contentPadding = PaddingValues(vertical = 4.dp)
            ) {
                items(destacadas, key = { it.id }) { especialidad ->
                    TarjetaEspecialidadDestacada(
                        especialidad = especialidad,
                        onClick = {
                            navController.navigate(Rutas.Medicos.crearRuta(especialidad.id))
                        }
                    )
                }
            }
        }
    }
}