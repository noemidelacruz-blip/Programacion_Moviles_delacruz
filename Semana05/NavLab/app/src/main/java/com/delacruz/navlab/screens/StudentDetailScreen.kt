package com.delacruz.navlab.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Domain
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.delacruz.navlab.model.StudentRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentDetailScreen(navController: NavController, studentId: Int = 1) {
    val student = StudentRepository.getStudentById(studentId)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Expediente Académico",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = Color.White
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF5E17EB)
                )
            )
        },
        containerColor = Color(0xFFF7F5FC)
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Bloque de cabecera superior con degradado morado intenso y esquinas inferiores muy redondeadas
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.TopCenter
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(130.dp)
                        .background(
                            brush = Brush.verticalGradient(
                                colors = listOf(Color(0xFF5E17EB), Color(0xFF8C33FF))
                            ),
                            shape = RoundedCornerShape(bottomStart = 36.dp, bottomEnd = 36.dp)
                        )
                )

                // Fotografía de perfil circular grande superpuesta en el borde inferior con borde exterior blanco
                Column(
                    modifier = Modifier.padding(top = 70.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(student.photoUrl)
                            .crossfade(enable = true)
                            .build(),
                        contentDescription = student.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(108.dp)
                            .clip(CircleShape)
                            .border(3.dp, Color.White, CircleShape)
                            .background(Color(0xFFE9D5FF))
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    // Nombre del estudiante en letra oscura negrita (ejemplo: "Juan León")
                    Text(
                        text = student.name,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF1F1035),
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    // Carrera en un tono morado más claro (ejemplo: "Ingeniería de Sistemas")
                    Text(
                        text = student.career,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF8C33FF),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Tarjeta Única de Datos (color gris claro/lavanda con esquinas muy redondeadas)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFFF3EEF9)),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // ID Estudiante
                    AcademicInfoItem(
                        icon = Icons.Default.Badge,
                        label = "ID Estudiante",
                        value = student.studentCode
                    )

                    // Correo
                    AcademicInfoItem(
                        icon = Icons.Default.Email,
                        label = "Correo",
                        value = student.email
                    )

                    // Facultad (sin repetir la palabra Facultad en el texto del valor)
                    AcademicInfoItem(
                        icon = Icons.Default.Domain,
                        label = "Facultad",
                        value = student.faculty
                    )

                    // Línea divisoria horizontal delgada
                    HorizontalDivider(
                        color = Color(0xFFE2D8F3),
                        thickness = 1.dp
                    )

                    // Sección Biografía
                    Column {
                        Text(
                            text = "Biografía",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF1F1035)
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = student.biography,
                            fontSize = 14.sp,
                            color = Color(0xFF4B5563),
                            lineHeight = 20.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

@Composable
private fun AcademicInfoItem(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Icono cuadrado morado sólido
        Box(
            modifier = Modifier
                .size(32.dp)
                .background(Color(0xFF7E22CE), RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color.White,
                modifier = Modifier.size(18.dp)
            )
        }

        Spacer(modifier = Modifier.width(14.dp))

        Column {
            Text(
                text = label,
                fontSize = 12.sp,
                color = Color.Gray,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = value,
                fontSize = 15.sp,
                color = Color(0xFF1F1035),
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
