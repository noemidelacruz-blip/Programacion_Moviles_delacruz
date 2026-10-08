package com.delacruz.saludpluscitas.ui.screens.agendamiento

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
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
import com.delacruz.saludpluscitas.ui.components.TarjetaEspecialidad
import com.delacruz.saludpluscitas.ui.theme.BordeSuave

@Composable
fun EspecialidadesScreen(
    navController: NavController
) {
    var busqueda by remember { mutableStateOf("") }
    // Se recalcula en cada letra que se escribe.
    val resultados = Repositorio.buscarEspecialidades(busqueda)

    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            BarraSuperior(
                titulo = "Especialidades",
                onAtras = { navController.popBackStack() }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
        ) {
            CampoBusqueda(
                valor = busqueda,
                onValorChange = { busqueda = it },
                placeholder = "Buscar especialidad..."
            )

            if (resultados.isEmpty()) {
                EstadoVacio(mensaje = "No se encontraron especialidades")
            } else {
                LazyColumn(
                    contentPadding = PaddingValues(vertical = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    items(resultados, key = { it.id }) { especialidad ->
                        TarjetaEspecialidad(
                            especialidad = especialidad,
                            onClick = {
                                navController.navigate(Rutas.Medicos.crearRuta(especialidad.id))
                            }
                        )
                        HorizontalDivider(color = BordeSuave)
                    }
                }
            }
        }
    }
}