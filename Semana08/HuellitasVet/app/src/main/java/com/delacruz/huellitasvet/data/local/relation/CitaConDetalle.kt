package com.delacruz.huellitasvet.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.delacruz.huellitasvet.data.local.entity.Cita
import com.delacruz.huellitasvet.data.local.entity.Mascota
import com.delacruz.huellitasvet.data.local.entity.Servicio

// Una cita con todo lo que la pantalla necesita mostrar: la mascota (y su dueno) y el servicio.
// La relacion con la mascota es anidada: Cita -> Mascota -> Usuario (por eso entity = Mascota::class)
data class CitaConDetalle(
    @Embedded val cita: Cita,
    @Relation(entity = Mascota::class, parentColumn = "mascotaId", entityColumn = "id")
    val mascota: MascotaConDueno,
    @Relation(parentColumn = "servicioId", entityColumn = "id")
    val servicio: Servicio
)