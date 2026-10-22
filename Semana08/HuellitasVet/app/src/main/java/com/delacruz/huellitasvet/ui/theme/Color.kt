package com.delacruz.huellitasvet.ui.theme

import androidx.compose.ui.graphics.Color
import com.delacruz.huellitasvet.model.EstadoCita

// ---------- Perfil Dueno (esmeralda) ----------
val EsmeraldaPrimario = Color(0xFF059669)
val EsmeraldaContenedor = Color(0xFFD1FAE5)
val EsmeraldaFondo = Color(0xFFF6FDF9)
val EsmeraldaTextoContenedor = Color(0xFF064E3B)

// ---------- Perfil Veterinario (azul) ----------
val AzulPrimario = Color(0xFF1D4ED8)
val AzulContenedor = Color(0xFFDBEAFE)
val AzulFondo = Color(0xFFF7F9FF)
val AzulTextoContenedor = Color(0xFF1E3A8A)

// ---------- Estados de la cita (iguales para ambos perfiles) ----------
val EstadoPendiente = Color(0xFFD97706)   // ambar
val EstadoConfirmada = Color(0xFF16A34A)  // verde
val EstadoAtendida = Color(0xFF475569)    // gris
val EstadoCancelada = Color(0xFFDC2626)   // rojo

// ---------- Colores auxiliares de apoyo ----------
val RojoAccion = Color(0xFFDC2626)        // botones Cancelar / Cerrar sesion
val RojoSuave = Color(0xFFFEE2E2)
val AmbarSuave = Color(0xFFFEF3C7)        // avisos (recordatorio, cancelar hasta 2 horas)
val AmbarTexto = Color(0xFF92400E)
val GrisSuave = Color(0xFFE2E8F0)         // contador "Atendidas", horarios ocupados
val TextoPrincipal = Color(0xFF0F172A)
val TextoSecundario = Color(0xFF64748B)
val Borde = Color(0xFFD7E3DD)

// ---------- Modo oscuro ----------
val EsmeraldaClaro = Color(0xFF34D399)
val EsmeraldaOscuroContenedor = Color(0xFF065F46)
val EsmeraldaOscuroFondo = Color(0xFF0B1410)
val EsmeraldaOscuroSuperficie = Color(0xFF13201A)

val AzulClaro = Color(0xFF60A5FA)
val AzulOscuroContenedor = Color(0xFF1E3A8A)
val AzulOscuroFondo = Color(0xFF0A0F1E)
val AzulOscuroSuperficie = Color(0xFF111A30)

val TextoClaroSobreOscuro = Color(0xFFE5E7EB)
val TextoSecundarioOscuro = Color(0xFF94A3B8)

// Devuelve el color del chip segun el estado de la cita
fun colorDeEstado(estado: EstadoCita): Color = when (estado) {
    EstadoCita.PENDIENTE -> EstadoPendiente
    EstadoCita.CONFIRMADA -> EstadoConfirmada
    EstadoCita.ATENDIDA -> EstadoAtendida
    EstadoCita.CANCELADA -> EstadoCancelada
}