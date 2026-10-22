package com.delacruz.huellitasvet.data.local

import com.delacruz.huellitasvet.data.local.entity.Cita
import com.delacruz.huellitasvet.data.local.entity.Mascota
import com.delacruz.huellitasvet.data.local.entity.Servicio
import com.delacruz.huellitasvet.data.local.entity.Usuario
import com.delacruz.huellitasvet.model.Especie
import com.delacruz.huellitasvet.model.EstadoCita
import com.delacruz.huellitasvet.model.Rol
import com.delacruz.huellitasvet.model.Sexo
import java.time.LocalDate

// Datos de prueba (todos ficticios, por la Ley 29733) que se cargan solo la primera vez
object DatosIniciales {

    suspend fun sembrar(db: VetDatabase) {
        // Si ya hay usuarios, la base ya fue sembrada y no se repite
        if (db.usuarioDao().contar() > 0) return

        // ---------- Servicios (el cuarto esta inactivo) ----------
        val consulta = Servicio(
            nombre = "Consulta general",
            descripcion = "Control de rutina y revisión general de la mascota",
            duracionMin = 30, precio = 60.0, icono = "heart"
        )
        val vacunacion = Servicio(
            nombre = "Vacunación",
            descripcion = "Vacunas según el calendario de la mascota",
            duracionMin = 30, precio = 45.0, icono = "syringe"
        )
        val bano = Servicio(
            nombre = "Baño y corte",
            descripcion = "Higiene general y corte de pelo",
            duracionMin = 60, precio = 50.0, icono = "scissors"
        )
        val desparasitacion = Servicio(
            nombre = "Desparasitación",
            descripcion = "Tratamiento antiparasitario interno y externo",
            duracionMin = 15, precio = 25.0, activo = false, icono = "heart"
        )
        val consultaId = db.servicioDao().insertar(consulta).toInt()
        val vacunacionId = db.servicioDao().insertar(vacunacion).toInt()
        val banoId = db.servicioDao().insertar(bano).toInt()
        db.servicioDao().insertar(desparasitacion)

        // ---------- Usuarios: 3 duenos y 1 veterinaria ----------
        val ids = db.usuarioDao().insertarTodos(
            listOf(
                Usuario(nombre = "Noemi De La Cruz", correo = "noemi@correo.test", clave = "1234", rol = Rol.CLIENTE.name),
                Usuario(nombre = "Luis Ramírez", correo = "luis@correo.test", clave = "1234", rol = Rol.CLIENTE.name),
                Usuario(nombre = "Marta Gómez", correo = "marta@correo.test", clave = "1234", rol = Rol.CLIENTE.name),
                Usuario(nombre = "Dra. Rojas", correo = "vet@huellitas.test", clave = "admin", rol = Rol.ADMIN.name)
            )
        )
        val anaId = ids[0].toInt()
        val luisId = ids[1].toInt()
        val martaId = ids[2].toInt()

        // ---------- Mascotas: el ano de nacimiento se calcula para que la edad sea siempre la misma ----------
        val anio = LocalDate.now().year
        val lunaId = db.mascotaDao().insertar(
            Mascota(duenoId = anaId, nombre = "Luna", especie = Especie.GATO.name, raza = "Siamés",
                anioNacimiento = anio - 3, peso = 4.3, sexo = Sexo.HEMBRA.name)
        ).toInt()
        val maxId = db.mascotaDao().insertar(
            Mascota(duenoId = anaId, nombre = "Max", especie = Especie.PERRO.name, raza = "Labrador",
                anioNacimiento = anio - 4, peso = 24.5, sexo = Sexo.MACHO.name)
        ).toInt()
        val rockyId = db.mascotaDao().insertar(
            Mascota(duenoId = luisId, nombre = "Rocky", especie = Especie.PERRO.name, raza = "Bulldog",
                anioNacimiento = anio - 6, peso = 22.0, sexo = Sexo.MACHO.name)
        ).toInt()
        val michiId = db.mascotaDao().insertar(
            Mascota(duenoId = martaId, nombre = "Michi", especie = Especie.GATO.name, raza = "Mestizo",
                anioNacimiento = anio - 2, peso = 3.8, sexo = Sexo.MACHO.name)
        ).toInt()

        // ---------- Citas: fechas relativas a hoy (negativo = pasado, positivo = futuro) ----------
        val hoy = LocalDate.now()
        val citaDao = db.citaDao()

        // Historial de Luna: 3 citas ATENDIDAS con diagnostico, tratamiento y peso
        citaDao.insertar(
            Cita(mascotaId = lunaId, servicioId = vacunacionId, fecha = hoy.minusDays(6).toString(), hora = "10:00",
                estado = EstadoCita.ATENDIDA.name, precio = vacunacion.precio,
                diagnostico = "Vacuna triple felina.", tratamiento = "Refuerzo en 12 meses.", pesoRegistrado = 4.3)
        )
        citaDao.insertar(
            Cita(mascotaId = lunaId, servicioId = consultaId, fecha = hoy.minusDays(107).toString(), hora = "09:30",
                estado = EstadoCita.ATENDIDA.name, precio = consulta.precio,
                diagnostico = "Control de rutina, sin hallazgos.", tratamiento = "Ninguno.", pesoRegistrado = 4.2)
        )
        citaDao.insertar(
            Cita(mascotaId = lunaId, servicioId = banoId, fecha = hoy.minusDays(217).toString(), hora = "11:00",
                estado = EstadoCita.ATENDIDA.name, precio = bano.precio,
                diagnostico = "Higiene general.", tratamiento = "Baño y corte de pelo.", pesoRegistrado = 4.0)
        )

        // Una cita pasada ATENDIDA de Max
        citaDao.insertar(
            Cita(mascotaId = maxId, servicioId = consultaId, fecha = hoy.minusDays(20).toString(), hora = "15:00",
                estado = EstadoCita.ATENDIDA.name, precio = consulta.precio,
                diagnostico = "Revisión general, buen estado.", tratamiento = "Ninguno.", pesoRegistrado = 24.2)
        )

        // Citas de hoy: una atendida y dos pendientes
        citaDao.insertar(
            Cita(mascotaId = maxId, servicioId = banoId, fecha = hoy.toString(), hora = "09:00",
                estado = EstadoCita.ATENDIDA.name, precio = bano.precio,
                diagnostico = "Baño completo sin novedades.", tratamiento = "Ninguno.", pesoRegistrado = 24.5)
        )
        citaDao.insertar(
            Cita(mascotaId = rockyId, servicioId = consultaId, fecha = hoy.toString(), hora = "11:00",
                estado = EstadoCita.PENDIENTE.name, precio = consulta.precio)
        )
        citaDao.insertar(
            Cita(mascotaId = michiId, servicioId = consultaId, fecha = hoy.toString(), hora = "12:30",
                estado = EstadoCita.PENDIENTE.name, precio = consulta.precio)
        )

        // Citas de los proximos dias: confirmadas, pendiente y una cancelada
        citaDao.insertar(
            Cita(mascotaId = lunaId, servicioId = vacunacionId, fecha = hoy.plusDays(1).toString(), hora = "10:30",
                estado = EstadoCita.CONFIRMADA.name, precio = vacunacion.precio, nota = "Vacuna anual")
        )
        citaDao.insertar(
            Cita(mascotaId = maxId, servicioId = vacunacionId, fecha = hoy.plusDays(2).toString(), hora = "15:00",
                estado = EstadoCita.PENDIENTE.name, precio = vacunacion.precio)
        )
        citaDao.insertar(
            Cita(mascotaId = michiId, servicioId = banoId, fecha = hoy.plusDays(3).toString(), hora = "16:00",
                estado = EstadoCita.CANCELADA.name, precio = bano.precio)
        )
        citaDao.insertar(
            Cita(mascotaId = rockyId, servicioId = vacunacionId, fecha = hoy.plusDays(4).toString(), hora = "14:00",
                estado = EstadoCita.CONFIRMADA.name, precio = vacunacion.precio)
        )
    }
}