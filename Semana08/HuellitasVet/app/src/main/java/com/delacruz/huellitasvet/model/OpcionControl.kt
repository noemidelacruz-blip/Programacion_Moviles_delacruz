package com.delacruz.huellitasvet.model

// Opciones de proximo control medico para la mascota
enum class OpcionControl(
    val etiqueta: String,
    val dias: Int?
) {
    NINGUNO("Sin control", null),
    SEMANA_1("En 1 semana", 7),
    SEMANAS_2("En 2 semanas", 14),
    MES_1("En 1 mes", 30),
    MESES_3("En 3 meses", 90)
}
