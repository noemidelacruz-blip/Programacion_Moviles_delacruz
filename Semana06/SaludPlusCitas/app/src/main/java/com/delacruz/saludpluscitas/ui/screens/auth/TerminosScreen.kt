package com.delacruz.saludpluscitas.ui.screens.auth

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.delacruz.saludpluscitas.ui.components.BarraSuperior
import com.delacruz.saludpluscitas.ui.theme.TextoSecundario

// Secciones del texto: pares de título y contenido.
private val secciones = listOf(
    "1. Aceptación de los términos" to
            "Al crear una cuenta en la aplicación de Clínica SaludPlus, el usuario declara haber leído y aceptado estos términos y condiciones.",
    "2. Uso de la aplicación" to
            "La aplicación permite consultar especialidades y médicos, agendar citas presenciales y revisar las citas registradas. El usuario se compromete a usarla de forma responsable.",
    "3. Registro y datos personales" to
            "El usuario debe proporcionar datos verdaderos al registrarse. El teléfono es obligatorio y el correo es opcional. Los datos se usan únicamente para gestionar las citas.",
    "4. Citas médicas" to
            "Cada cita dura 30 minutos y se atiende de forma presencial. Se recomienda llegar 15 minutos antes del horario reservado. Un horario ya reservado no puede ser tomado por otro usuario.",
    "5. Cancelación de citas" to
            "El usuario puede cancelar sus citas desde el detalle de la cita. Al cancelar, el horario vuelve a quedar disponible.",
    "6. Seguridad de la cuenta" to
            "El usuario es responsable de mantener la confidencialidad de su contraseña y de cerrar sesión al terminar de usar la aplicación.",
    "7. Modificaciones" to
            "La clínica puede actualizar estos términos en cualquier momento. El uso continuo de la aplicación implica la aceptación de los cambios."
)

@Composable
fun TerminosScreen(
    navController: NavController
) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.surface,
        topBar = {
            BarraSuperior(
                titulo = "Términos y condiciones",
                onAtras = { navController.popBackStack() }
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
            secciones.forEach { (titulo, contenido) ->
                Text(
                    text = titulo,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = contenido,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    color = TextoSecundario
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}