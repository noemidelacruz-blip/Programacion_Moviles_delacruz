package com.delacruz.saludpluscitas.data.repository

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.delacruz.saludpluscitas.data.model.Cita
import com.delacruz.saludpluscitas.data.model.Especialidad
import com.delacruz.saludpluscitas.data.model.Medico
import com.delacruz.saludpluscitas.data.model.Usuario

// Todos los datos viven en memoria (sin Room, SQLite ni Firebase).
// Se pierden al cerrar la app.
object Repositorio {

    // ---------------------------------------------------------------
    // Colecciones
    // ---------------------------------------------------------------

    // Usuario de prueba para poder iniciar sesión sin registrarse.
    val usuarios = mutableStateListOf(
        Usuario(
            id = 1,
            nombre = "Noemi De la Cruz",
            telefono = "989821225",
            correo = "noemi.delacruz@gmail.com",
            password = "123456"
        )
    )

    var usuarioActual by mutableStateOf<Usuario?>(null)

    val especialidades = listOf(
        Especialidad(1, "Medicina General", "Atención integral"),
        Especialidad(2, "Pediatría", "Niños y adolescentes"),
        Especialidad(3, "Ginecología", "Salud de la mujer"),
        Especialidad(4, "Cardiología", "Corazón y vasos sanguíneos"),
        Especialidad(5, "Dermatología", "Piel, cabello y uñas"),
        Especialidad(6, "Traumatología", "Huesos y articulaciones"),
        Especialidad(7, "Oftalmología", "Salud visual")
    )

    private const val DIRECCION = "Av. Los Olivos 123, Lima"

    val medicos = listOf(
        // Medicina General
        Medico(1, "Dr. Carlos Mendoza", 1, "Médico general", "10234", 4.7, 102, "Disponible hoy", DIRECCION),
        Medico(2, "Dra. Lucía Fernández", 1, "Médica general", "10876", 4.5, 64, "Disponible mañana", DIRECCION),
        // Pediatría
        Medico(3, "Dra. Patricia Vargas", 2, "Pediatra", "11452", 4.9, 140, "Disponible hoy", DIRECCION),
        Medico(4, "Dr. Jorge Salazar", 2, "Pediatra", "11987", 4.6, 71, "Disponible esta semana", DIRECCION),
        // Ginecología (datos del diseño de referencia)
        Medico(5, "Dra. Ana Torres", 3, "Ginecóloga", "12345", 4.9, 120, "Disponible hoy", DIRECCION),
        Medico(6, "Dra. Claudia Rojas", 3, "Ginecóloga", "12678", 4.8, 95, "Disponible mañana", DIRECCION),
        Medico(7, "Dr. Luis Ramírez", 3, "Ginecólogo", "12901", 4.7, 88, "Disponible hoy", DIRECCION),
        Medico(8, "Dra. Mariana Soto", 3, "Ginecóloga", "13024", 4.6, 76, "Disponible esta semana", DIRECCION),
        // Cardiología
        Medico(9, "Dr. Ricardo Paredes", 4, "Cardiólogo", "13456", 4.8, 110, "Disponible mañana", DIRECCION),
        Medico(10, "Dra. Elena Castro", 4, "Cardióloga", "13789", 4.7, 83, "Disponible hoy", DIRECCION),
        // Dermatología
        Medico(11, "Dra. Sofía Quispe", 5, "Dermatóloga", "14123", 4.8, 97, "Disponible hoy", DIRECCION),
        Medico(12, "Dr. Martín Herrera", 5, "Dermatólogo", "14456", 4.4, 52, "Disponible esta semana", DIRECCION),
        // Traumatología
        Medico(13, "Dr. Andrés Flores", 6, "Traumatólogo", "14789", 4.7, 91, "Disponible mañana", DIRECCION),
        Medico(14, "Dra. Valeria Ríos", 6, "Traumatóloga", "15012", 4.6, 68, "Disponible hoy", DIRECCION),
        // Oftalmología
        Medico(15, "Dr. Fernando Chávez", 7, "Oftalmólogo", "15345", 4.8, 105, "Disponible esta semana", DIRECCION),
        Medico(16, "Dra. Gabriela Núñez", 7, "Oftalmóloga", "15678", 4.5, 59, "Disponible mañana", DIRECCION)
    )

    // Turnos de 30 minutos que atiende cada médico.
    val horariosBase = listOf(
        "08:00", "08:30", "09:00",
        "09:30", "10:00", "10:30",
        "11:00", "11:30", "12:00"
    )

    val citas = mutableStateListOf<Cita>()

    // ---------------------------------------------------------------
    // Usuarios y sesión
    // ---------------------------------------------------------------

    // Registra un usuario nuevo y deja su sesión iniciada. Devuelve false
    // si el teléfono (o el correo, si se llenó) ya está registrado.
    fun registrarUsuario(
        nombre: String,
        telefono: String,
        correo: String,
        password: String
    ): Boolean {
        val tel = telefono.trim()
        val email = correo.trim()

        val existe = usuarios.any {
            it.telefono == tel || (email.isNotBlank() && it.correo.equals(email, ignoreCase = true))
        }
        if (existe) return false

        val nuevo = Usuario(
            id = (usuarios.maxOfOrNull { it.id } ?: 0) + 1,
            nombre = nombre.trim(),
            telefono = tel,
            correo = email,
            password = password
        )
        usuarios.add(nuevo)
        usuarioActual = nuevo
        return true
    }

    // Busca al usuario por teléfono o correo y contraseña.
    // Si existe, lo guarda en usuarioActual y devuelve true.
    fun iniciarSesion(usuario: String, password: String): Boolean {
        val dato = usuario.trim()
        val encontrado = usuarios.find {
            (it.telefono == dato ||
                    (it.correo.isNotBlank() && it.correo.equals(dato, ignoreCase = true))) &&
                    it.password == password
        }
        usuarioActual = encontrado
        return encontrado != null
    }

    fun cerrarSesion() {
        usuarioActual = null
    }

    // ---------------------------------------------------------------
    // Especialidades y médicos
    // ---------------------------------------------------------------

    // Especialidades cuyo nombre contiene el texto (sin importar mayúsculas).
    fun buscarEspecialidades(texto: String): List<Especialidad> {
        val dato = texto.trim()
        return especialidades.filter { it.nombre.contains(dato, ignoreCase = true) }
    }

    // Las 3 primeras especialidades para el LazyRow del Inicio.
    fun especialidadesDestacadas(): List<Especialidad> {
        return especialidades.take(3)
    }

    fun obtenerEspecialidad(id: Int): Especialidad? {
        return especialidades.find { it.id == id }
    }

    fun obtenerMedico(id: Int): Medico? {
        return medicos.find { it.id == id }
    }

    fun obtenerCita(id: Int): Cita? {
        return citas.find { it.id == id }
    }

    // Médicos de una especialidad, mejor calificados primero.
    fun medicosPorEspecialidad(especialidadId: Int): List<Medico> {
        return medicos
            .filter { it.especialidadId == especialidadId }
            .sortedByDescending { it.calificacion }
    }

    // Médicos de una especialidad cuyo nombre contiene el texto.
    fun buscarMedicos(especialidadId: Int, texto: String): List<Medico> {
        val dato = texto.trim()
        return medicosPorEspecialidad(especialidadId)
            .filter { it.nombre.contains(dato, ignoreCase = true) }
    }

    // ---------------------------------------------------------------
    // Citas
    // ---------------------------------------------------------------

    // Horarios de horariosBase que el médico aún no tiene reservados ese día.
    fun horariosDisponibles(medicoId: Int, fecha: String): List<String> {
        val ocupadas = citas
            .filter { it.medicoId == medicoId && it.fecha == fecha }
            .map { it.hora }
        return horariosBase.filter { it !in ocupadas }
    }

    // Crea la cita del usuarioActual. Devuelve null si ese horario
    // ya está tomado o si no hay sesión iniciada.
    fun agendarCita(
        medicoId: Int,
        fecha: String,
        hora: String,
        motivo: String
    ): Cita? {
        val usuario = usuarioActual ?: return null

        val ocupado = citas.any {
            it.medicoId == medicoId && it.fecha == fecha && it.hora == hora
        }
        if (ocupado) return null

        val cita = Cita(
            id = (citas.maxOfOrNull { it.id } ?: 0) + 1,
            usuarioId = usuario.id,
            medicoId = medicoId,
            fecha = fecha,
            hora = hora,
            motivo = motivo.trim()
        )
        citas.add(cita)
        return cita
    }

    // Citas del usuarioActual ordenadas por fecha y hora.
    fun citasDelUsuario(): List<Cita> {
        val usuario = usuarioActual ?: return emptyList()
        return citas
            .filter { it.usuarioId == usuario.id }
            .sortedWith(compareBy({ it.fecha }, { it.hora }))
    }

    // Reto extra (Detalle de cita).
    // Elimina la cita por id. Devuelve true si se eliminó alguna cita.
    fun cancelarCita(id: Int): Boolean {
        return citas.removeIf { it.id == id }
    }
}