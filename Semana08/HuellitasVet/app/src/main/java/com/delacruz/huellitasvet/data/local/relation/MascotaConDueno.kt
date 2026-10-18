package com.delacruz.huellitasvet.data.local.relation

import androidx.room.Embedded
import androidx.room.Relation
import com.delacruz.huellitasvet.data.local.entity.Mascota
import com.delacruz.huellitasvet.data.local.entity.Usuario

// Una mascota junto con su dueno. Se usa en la lista de Pacientes del veterinario
data class MascotaConDueno(
    @Embedded val mascota: Mascota,
    @Relation(parentColumn = "duenoId", entityColumn = "id")
    val dueno: Usuario
)