package com.delacruz.huellitasvet.data.local

import com.delacruz.huellitasvet.data.local.entity.Servicio

// Datos de prueba (ficticios) que se cargan solo la primera vez que se abre la app.
// Por ahora solo siembra los servicios; en el commit 4 se agregan usuarios, mascotas y citas
object DatosIniciales {

    suspend fun sembrar(db: VetDatabase) {
        // Si ya hay servicios, no se vuelve a sembrar
        if (db.servicioDao().contar() > 0) return

        db.servicioDao().insertarTodos(
            listOf(
                Servicio(
                    nombre = "Consulta general",
                    descripcion = "Control de rutina y revisión general de la mascota",
                    duracionMin = 30,
                    precio = 60.0,
                    icono = "heart"
                ),
                Servicio(
                    nombre = "Vacunación",
                    descripcion = "Vacunas según el calendario de la mascota",
                    duracionMin = 30,
                    precio = 45.0,
                    icono = "syringe"
                ),
                Servicio(
                    nombre = "Baño y corte",
                    descripcion = "Higiene general y corte de pelo",
                    duracionMin = 60,
                    precio = 50.0,
                    icono = "scissors"
                ),
                // Servicio inactivo: no debe aparecer al pedir una cita
                Servicio(
                    nombre = "Desparasitación",
                    descripcion = "Tratamiento antiparasitario interno y externo",
                    duracionMin = 15,
                    precio = 25.0,
                    activo = false,
                    icono = "heart"
                )
            )
        )
    }
}