package com.delacruz.saludpluscitas.navigation

sealed class Rutas(val ruta: String) {

    // auth
    object Splash : Rutas("splash")
    object Registro : Rutas("registro")
    object Login : Rutas("login")
    object Terminos : Rutas("terminos")

    // home
    object Home : Rutas("home")

    // agendamiento
    object Especialidades : Rutas("especialidades")


    object Medicos : Rutas("medicos/{especialidadId}") {
        fun crearRuta(especialidadId: Int): String {
            return "medicos/$especialidadId"
        }
    }

    object FechaHora : Rutas("fecha_hora/{medicoId}") {
        fun crearRuta(medicoId: Int): String {
            return "fecha_hora/$medicoId"
        }
    }

    object ConfirmarCita : Rutas("confirmar_cita/{medicoId}/{fecha}/{hora}") {
        fun crearRuta(medicoId: Int, fecha: String, hora: String): String {
            return "confirmar_cita/$medicoId/$fecha/$hora"
        }
    }

    object CitaExitosa : Rutas("cita_exitosa/{citaId}") {
        fun crearRuta(citaId: Int): String {
            return "cita_exitosa/$citaId"
        }
    }

    // citas
    object MisCitas : Rutas("mis_citas")

    object DetalleCita : Rutas("detalle_cita/{citaId}") {
        fun crearRuta(citaId: Int): String {
            return "detalle_cita/$citaId"
        }
    }

    // perfil, resultados y notificaciones
    object Perfil : Rutas("perfil")
    object Resultados : Rutas("resultados")
    object Notificaciones : Rutas("notificaciones")
}