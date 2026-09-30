package com.delacruz.navlab.model

data class Student(
    val id: Int,
    val name: String,
    val career: String,
    val studentCode: String,
    val email: String,
    val faculty: String,
    val photoUrl: String,
    val biography: String = "Estudiante destacado por interés en desarrollo Android."
)

object StudentRepository {
    val students = listOf(
        Student(
            id = 1,
            name = "Juan León",
            career = "Ingeniería de Sistemas",
            studentCode = "2024-0001",
            email = "juan.leon@example.com",
            faculty = "Ingeniería y Tecnología",
            photoUrl = "https://images.unsplash.com/photo-1539571696357-5a69c17a67c6?auto=format&fit=crop&w=400&q=80",
            biography = "Estudiante destacado por interés en desarrollo Android."
        ),
        Student(
            id = 2,
            name = "Maria Garcia",
            career = "Arquitectura",
            studentCode = "2024-0002",
            email = "maria.garcia@example.com",
            faculty = "Diseño y Arquitectura",
            photoUrl = "https://images.unsplash.com/photo-1494790108377-be9c29b29330?auto=format&fit=crop&w=400&q=80",
            biography = "Apasionada por el diseño de espacios sostenibles y modelado 3D."
        ),
        Student(
            id = 3,
            name = "Carlos Perez",
            career = "Medicina",
            studentCode = "2024-0003",
            email = "carlos.perez@example.com",
            faculty = "Ciencias de la Salud",
            photoUrl = "https://images.unsplash.com/photo-1507003211169-0a1dd7228f2d?auto=format&fit=crop&w=400&q=80",
            biography = "Enfocado en investigación médica y atención comunitaria."
        ),
        Student(
            id = 4,
            name = "Ana Lopez",
            career = "Derecho",
            studentCode = "2024-0004",
            email = "ana.lopez@example.com",
            faculty = "Derecho y Ciencias Políticas",
            photoUrl = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=400&q=80",
            biography = "Especializada en derecho digital y protección de datos."
        ),
        Student(
            id = 5,
            name = "Luis Ramirez",
            career = "Administración",
            studentCode = "2024-0005",
            email = "luis.ramirez@example.com",
            faculty = "Gestión y Negocios",
            photoUrl = "https://images.unsplash.com/photo-1500648767791-00dcc994a43e?auto=format&fit=crop&w=400&q=80",
            biography = "Interesado en innovación empresarial y finanzas corporativas."
        )
    )

    fun getStudentById(id: Int): Student {
        return students.find { it.id == id } ?: students.first()
    }
}
