package com.delacruz.navlab.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.delacruz.navlab.navigation.Screen

@Composable
fun HomeScreen(navController: NavController, userName: String = "Minji") {
    // Fondo general: Degradado vertical completo y continuo (morado pastel arriba a blanco en la base)
    val backgroundGradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFB39DDB), // Morado pastel / lila suave
            Color(0xFFD1C4E9),
            Color(0xFFEDE7F6),
            Color(0xFFFBF9FE),
            Color.White
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundGradient)
            .statusBarsPadding()
            .navigationBarsPadding()
            .padding(horizontal = 24.dp, vertical = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // Sección Superior: Saludo y Subtítulo
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 36.dp)
        ) {
            // Saludo dinámico limpio en dos líneas (sin signos '+')
            Text(
                text = "Bienvenido,",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Text(
                text = if (userName.isNotBlank()) userName else "Minji",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            // Espaciado vertical amplio
            Spacer(modifier = Modifier.height(28.dp))

            // Subtítulo en blanco semi-transparente
            Text(
                text = "¿Qué deseas gestionar hoy?",
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.88f),
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Medium
            )
        }

        // Sección Central: Tarjetas de Menú
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Tarjeta 1: Directorio de Alumnos
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(Screen.Directory.route) },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFFF3E8FF), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.School,
                            contentDescription = "Directorio",
                            tint = Color(0xFF6A1B9A),
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Directorio de Alumnos",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D1537)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Ver y gestionar estudiantes",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Ir",
                        tint = Color(0xFFB39DDB)
                    )
                }
            }

            // Tarjeta 2: Mi Perfil Académico
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { navController.navigate(Screen.Profile.route) },
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFFF3E8FF), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Perfil",
                            tint = Color(0xFF6A1B9A),
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Mi Perfil Académico",
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF2D1537)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Datos personales y progreso",
                            fontSize = 13.sp,
                            color = Color.Gray
                        )
                    }

                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "Ir",
                        tint = Color(0xFFB39DDB)
                    )
                }
            }
        }

        // Pie de página: Cerrar Sesión Segura en rojo sobre fondo blanco
        Row(
            modifier = Modifier
                .clickable {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
                .padding(bottom = 12.dp, top = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                contentDescription = "Cerrar sesión",
                tint = Color(0xFFD32F2F),
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "Cerrar Sesión Segura",
                color = Color(0xFFD32F2F),
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
