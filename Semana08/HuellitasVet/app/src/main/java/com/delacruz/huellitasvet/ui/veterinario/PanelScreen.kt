package com.delacruz.huellitasvet.ui.veterinario

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.delacruz.huellitasvet.ui.components.fechaCorta
import com.delacruz.huellitasvet.ui.components.graficos.GraficoBarras
import com.delacruz.huellitasvet.ui.components.graficos.GraficoDona
import com.delacruz.huellitasvet.ui.theme.AmbarSuave
import com.delacruz.huellitasvet.ui.theme.AmbarTexto
import com.delacruz.huellitasvet.ui.theme.EstadoAtendida
import com.delacruz.huellitasvet.ui.theme.EstadoPendiente
import com.delacruz.huellitasvet.ui.theme.GrisSuave
import com.delacruz.huellitasvet.viewmodel.PanelViewModel
import java.util.Locale

// Panel de la clinica del veterinario. La View solo observa el ViewModel y avisa de los clics
@Composable
fun PanelScreen(
    vm: PanelViewModel,
    onVerAgenda: () -> Unit,
    modifier: Modifier = Modifier
) {
    val citasHoy by vm.citasHoy.collectAsState()
    val pendientes by vm.pendientes.collectAsState()
    val atendidas by vm.atendidas.collectAsState()
    val ingresos by vm.ingresosMes.collectAsState()
    val atenciones by vm.atencionesMes.collectAsState()
    val porEstado by vm.porEstado.collectAsState()
    val porServicio by vm.porServicio.collectAsState()

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Encabezado con la fecha de hoy y la campana (sin pantalla de notificaciones en el laboratorio)
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = "Panel de la clínica",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Hoy · ${fechaCorta(vm.hoy.toString())}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
            IconButton(onClick = {}) {
                Icon(
                    imageVector = Icons.Filled.Notifications,
                    contentDescription = "Notificaciones",
                    tint = MaterialTheme.colorScheme.primary
                )
            }
        }

        // Tres contadores de color
        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
            Contador(
                valor = citasHoy,
                etiqueta = "Citas hoy",
                fondo = MaterialTheme.colorScheme.primaryContainer,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.weight(1f)
            )
            Contador(
                valor = pendientes,
                etiqueta = "Pendientes",
                fondo = AmbarSuave,
                color = EstadoPendiente,
                modifier = Modifier.weight(1f)
            )
            Contador(
                valor = atendidas,
                etiqueta = "Atendidas",
                fondo = GrisSuave,
                color = EstadoAtendida,
                modifier = Modifier.weight(1f)
            )
        }

        // Tarjeta azul de ingresos del mes
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = "Ingresos del mes",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Bottom,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    // %,.2f separa los miles con coma y usa punto decimal (S/ 3,420.00)
                    Text(
                        text = "S/ " + String.format(Locale.US, "%,.2f", ingresos),
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = if (atenciones == 1) "1 atención" else "$atenciones atenciones",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                }
            }
        }

        // Dona: citas por estado
        Text(
            text = "Citas por estado",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        TarjetaGrafico { GraficoDona(datos = porEstado) }

        // Barras: citas por servicio
        Text(
            text = "Citas por servicio",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        TarjetaGrafico { GraficoBarras(datos = porServicio) }

        // Alerta de citas por confirmar (solo si hay pendientes)
        if (pendientes > 0) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = AmbarSuave)
            ) {
                Row(
                    modifier = Modifier.padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.Warning,
                        contentDescription = null,
                        tint = EstadoPendiente
                    )
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = if (pendientes == 1) "1 cita por confirmar"
                            else "$pendientes citas por confirmar",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = AmbarTexto
                        )
                        Text(
                            text = "Revisa la agenda",
                            style = MaterialTheme.typography.bodySmall,
                            color = AmbarTexto
                        )
                    }
                    Button(
                        onClick = onVerAgenda,
                        shape = RoundedCornerShape(50),
                        contentPadding = PaddingValues(horizontal = 22.dp, vertical = 0.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = EstadoPendiente,
                            contentColor = Color.White
                        )
                    ) {
                        Text(text = "Ver", fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

// Contador de color: numero grande arriba y nombre abajo
@Composable
private fun Contador(
    valor: Int,
    etiqueta: String,
    fondo: Color,
    color: Color,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .background(color = fondo, shape = RoundedCornerShape(18.dp))
            .padding(vertical = 18.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = valor.toString(),
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = color
        )
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold,
            color = color
        )
    }
}

// Tarjeta blanca con borde donde van los graficos
@Composable
private fun TarjetaGrafico(contenido: @Composable () -> Unit) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.outline),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) { contenido() }
    }
}