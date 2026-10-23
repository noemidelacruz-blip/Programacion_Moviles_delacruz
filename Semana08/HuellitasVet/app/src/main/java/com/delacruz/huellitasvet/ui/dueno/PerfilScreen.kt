package com.delacruz.huellitasvet.ui.dueno

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
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
import com.delacruz.huellitasvet.data.local.entity.Usuario
import com.delacruz.huellitasvet.model.Rol
import com.delacruz.huellitasvet.ui.components.AppTopBar
import com.delacruz.huellitasvet.ui.components.DivisorPreferencias
import com.delacruz.huellitasvet.ui.components.FilaPreferencia
import com.delacruz.huellitasvet.ui.components.GrupoPreferencias
import com.delacruz.huellitasvet.ui.components.NotaDataStore
import com.delacruz.huellitasvet.ui.components.TarjetaCambiarPerfil
import com.delacruz.huellitasvet.ui.components.TarjetaUsuario
import com.delacruz.huellitasvet.ui.components.VetBotonTipo
import com.delacruz.huellitasvet.ui.components.VetButton
import com.delacruz.huellitasvet.ui.theme.AmbarSuave
import com.delacruz.huellitasvet.ui.theme.AzulContenedor
import com.delacruz.huellitasvet.ui.theme.AzulPrimario
import com.delacruz.huellitasvet.ui.theme.EstadoPendiente
import com.delacruz.huellitasvet.viewmodel.PerfilViewModel

// Perfil del dueno: datos del usuario, contadores, cambio de perfil y preferencias.
// La View solo observa el ViewModel y avisa de los clics; los ajustes viven en DataStore
@Composable
fun PerfilScreen(
    vm: PerfilViewModel,
    usuario: Usuario,
    modoOscuro: Boolean,
    recordatorios: Boolean,
    onModoOscuro: (Boolean) -> Unit,
    onRecordatorios: (Boolean) -> Unit,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    val mascotas by vm.mascotas.collectAsState()
    val proximas by vm.proximas.collectAsState()
    val atenciones by vm.atenciones.collectAsState()

    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(titulo = "Perfil", onAtras = onVolver)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            TarjetaUsuario(nombre = usuario.nombre, correo = usuario.correo, chip = "Dueño", rol = Rol.CLIENTE)

            // Tres contadores de color: verde, ambar y azul
            Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                Contador(
                    valor = mascotas,
                    etiqueta = "Mascotas",
                    fondo = MaterialTheme.colorScheme.primaryContainer,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.weight(1f)
                )
                Contador(
                    valor = proximas,
                    etiqueta = "Próximas",
                    fondo = AmbarSuave,
                    color = EstadoPendiente,
                    modifier = Modifier.weight(1f)
                )
                Contador(
                    valor = atenciones,
                    etiqueta = "Atenciones",
                    fondo = AzulContenedor,
                    color = AzulPrimario,
                    modifier = Modifier.weight(1f)
                )
            }

            TarjetaCambiarPerfil(
                subtitulo = "Modo práctica: ir a vista Veterinario",
                onClick = onCambiarPerfil
            )

            Text(
                text = "Preferencias",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )
            GrupoPreferencias {
                FilaPreferencia(
                    titulo = "Modo oscuro",
                    subtitulo = "Cambia el tema de la app",
                    activado = modoOscuro,
                    onCambio = onModoOscuro
                )
                DivisorPreferencias()
                FilaPreferencia(
                    titulo = "Recordatorios",
                    subtitulo = "Avisar citas y vacunas",
                    activado = recordatorios,
                    onCambio = onRecordatorios
                )
            }

            NotaDataStore()

            VetButton(
                texto = "Cerrar sesión",
                onClick = onCerrarSesion,
                tipo = VetBotonTipo.CONTORNO,
                color = MaterialTheme.colorScheme.error
            )
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