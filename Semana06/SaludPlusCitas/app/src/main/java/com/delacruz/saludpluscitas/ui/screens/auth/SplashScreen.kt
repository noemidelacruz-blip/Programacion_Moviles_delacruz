package com.delacruz.saludpluscitas.ui.screens.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Spa
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.delacruz.saludpluscitas.navigation.Rutas
import com.delacruz.saludpluscitas.ui.components.BotonPrimario
import com.delacruz.saludpluscitas.ui.theme.AzulClaro
import com.delacruz.saludpluscitas.ui.theme.AzulOscuro
import com.delacruz.saludpluscitas.ui.theme.AzulPrimario
import com.delacruz.saludpluscitas.ui.theme.TextoSecundario

private val FondoSplash = Color(0xFFF5F6FA)
private val HojaVerde = Color(0xFF5BA889)
private val HojaAzul = Color(0xFF7FB3D5)

@Composable
fun SplashScreen(
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(FondoSplash)
            .safeDrawingPadding()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(32.dp))

        LogoSaludPlus()

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Clínica",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = AzulOscuro
        )
        Text(
            text = "SaludPlus",
            fontSize = 36.sp,
            fontWeight = FontWeight.ExtraBold,
            color = AzulOscuro
        )
        Text(
            text = "Tu salud, nuestra prioridad",
            fontSize = 15.sp,
            color = TextoSecundario
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.Center
        ) {
            IlustracionDoctor()
        }

        BotonPrimario(
            texto = "Comenzar",
            onClick = { navController.navigate(Rutas.Registro.ruta) }
        )

        TextButton(onClick = { navController.navigate(Rutas.Login.ruta) }) {
            Text(
                text = "Ya tengo una cuenta",
                color = AzulPrimario,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}

// Ilustración del doctor armada con íconos de Material:
// doctor al centro, maletín médico en una insignia y hojas a los lados.
@Composable
private fun IlustracionDoctor() {
    Box(
        modifier = Modifier.size(260.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(230.dp)
                .clip(CircleShape)
                .background(AzulClaro.copy(alpha = 0.6f))
        )
        Box(
            modifier = Modifier
                .size(170.dp)
                .clip(CircleShape)
                .background(AzulClaro),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Doctor",
                tint = AzulPrimario,
                modifier = Modifier.size(130.dp)
            )
        }
        Icon(
            imageVector = Icons.Default.Spa,
            contentDescription = null,
            tint = HojaVerde,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .size(64.dp)
        )
        Icon(
            imageVector = Icons.Default.Spa,
            contentDescription = null,
            tint = HojaAzul,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .size(56.dp)
        )
        Box(
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 28.dp, end = 28.dp)
                .size(56.dp)
                .clip(CircleShape)
                .background(Color.White),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.MedicalServices,
                contentDescription = null,
                tint = AzulPrimario,
                modifier = Modifier.size(30.dp)
            )
        }
    }
}

// Cruz azul con un corazón blanco al centro.
@Composable
private fun LogoSaludPlus() {
    Box(
        modifier = Modifier.size(96.dp),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .width(36.dp)
                .height(96.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(AzulPrimario)
        )
        Box(
            modifier = Modifier
                .width(96.dp)
                .height(36.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(AzulPrimario)
        )
        Icon(
            imageVector = Icons.Default.Favorite,
            contentDescription = null,
            tint = Color.White,
            modifier = Modifier.size(30.dp)
        )
    }
}