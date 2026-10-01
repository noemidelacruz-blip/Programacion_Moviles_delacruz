package com.delacruz.clinicasalud.navigation

// Clases de datos para el proyecto
data class Doctor(
    val id: Int,
    val nombre: String,
    val especialidad: String,
    val calificacion: Double,
    val resenas: Int,
    val experiencia: String,
    val biografia: String
)

data class Cita(
    val doctorNombre: String,
    val fecha: String,
    val hora: String,
    val estado: String // "Confirmada" o "Completada"
)

// Datos iniciales de prueba
val listaMedicosPrueba = listOf(
    Doctor(1, "Dra. Ana Torres", "Cardiología", 4.9, 128, "12 años exp.", "Especialista en arritmias e hipertensión."),
    Doctor(2, "Dr. Luis Vega", "Pediatría", 4.7, 95, "8 años exp.", "Atención integral infantil y pediatría preventiva."),
    Doctor(3, "Dra. Rosa Díaz", "Dermatología", 4.8, 110, "10 años exp.", "Especialista en dermatología clínica y estética.")
)

// Definición de las rutas de navegación
sealed class Screen(val route: String) {
    object Home : Screen("home")
    object DoctorDetail : Screen("doctor_detail/{doctorId}") {
        fun createRoute(doctorId: Int) = "doctor_detail/$doctorId"
    }
    object Appointment : Screen("appointment/{doctorId}") {
        fun createRoute(doctorId: Int) = "appointment/$doctorId"
    }
    object Confirmation : Screen("confirmation/{doctorNombre}/{fecha}/{hora}") {
        fun createRoute(doctorNombre: String, fecha: String, hora: String) =
            "confirmation/$doctorNombre/$fecha/$hora"
    }
    object Appointments : Screen("appointments")
    object MedicalHistory : Screen("medical_history")
    object Profile : Screen("profile")

}