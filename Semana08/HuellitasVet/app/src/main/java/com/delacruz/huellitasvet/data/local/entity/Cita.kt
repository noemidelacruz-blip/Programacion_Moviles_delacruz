package com.delacruz.huellitasvet.data.local.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.delacruz.huellitasvet.model.EstadoCita

// Cita de una mascota para un servicio (tabla citas).
// - fecha se guarda como texto "aaaa-MM-dd" y hora como "HH:mm": asi se ordenan bien
//   y se comparan con = en SQL, y se convierten con LocalDate.parse() y LocalTime.parse()
// - precio es una "foto" del precio del servicio al pedir la cita: si luego cambia el
//   precio del servicio, las citas antiguas conservan lo que costaron
// - diagnostico, tratamiento y pesoRegistrado quedan nulos hasta que el veterinario
//   registra la atencion
@Entity(
    tableName = "citas",
    foreignKeys = [
        ForeignKey(
            entity = Mascota::class,
            parentColumns = ["id"],
            childColumns = ["mascotaId"]
        ),
        ForeignKey(
            entity = Servicio::class,
            parentColumns = ["id"],
            childColumns = ["servicioId"]
        )
    ],
    indices = [
        Index("mascotaId"),
        Index("servicioId"),
        // Acelera la consulta de horarios ocupados de un dia
        Index("fecha", "hora")
    ]
)
data class Cita(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val mascotaId: Int,
    val servicioId: Int,
    val fecha: String,
    val hora: String,
    val nota: String = "",
    val estado: String = EstadoCita.PENDIENTE.name,
    val precio: Double,
    val diagnostico: String? = null,
    val tratamiento: String? = null,
    val pesoRegistrado: Double? = null,
    val fechaControl: String? = null,
    val fechaCreacion: Long = System.currentTimeMillis()
)