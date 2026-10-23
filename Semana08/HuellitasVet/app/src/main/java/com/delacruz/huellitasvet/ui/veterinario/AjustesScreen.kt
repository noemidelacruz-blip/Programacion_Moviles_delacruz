package com.delacruz.huellitasvet.ui.veterinario

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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

// Ajustes del veterinario: datos del usuario, cambio de perfil y preferencias guardadas en DataStore.
// La View solo muestra los valores y avisa de los clics
@Composable
fun AjustesScreen(
    usuario: Usuario,
    modoOscuro: Boolean,
    notificaciones: Boolean,
    aceptarCitas: Boolean,
    onModoOscuro: (Boolean) -> Unit,
    onNotificaciones: (Boolean) -> Unit,
    onAceptarCitas: (Boolean) -> Unit,
    onCambiarPerfil: () -> Unit,
    onCerrarSesion: () -> Unit,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxSize()) {
        AppTopBar(titulo = "Ajustes", onAtras = onVolver)

        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            TarjetaUsuario(nombre = usuario.nombre, correo = usuario.correo, chip = "Perfil activo", rol = Rol.ADMIN)

            TarjetaCambiarPerfil(
                subtitulo = "Modo práctica: ir a vista Dueño",
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
                    titulo = "Notificaciones",
                    subtitulo = "Avisar citas nuevas",
                    activado = notificaciones,
                    onCambio = onNotificaciones
                )
                DivisorPreferencias()
                FilaPreferencia(
                    titulo = "Aceptar citas",
                    subtitulo = "Abrir o cerrar la agenda",
                    activado = aceptarCitas,
                    onCambio = onAceptarCitas
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